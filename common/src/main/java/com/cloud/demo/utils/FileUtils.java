package com.cloud.demo.utils;

import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.enums.FileNameGenerateStrategy;
import com.cloud.demo.exception.CommonException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @Author weiwei
 * @Date 2022/7/29 下午9:44
 * @Version 1.0
 * @Desc 文件处理工具类
 */
public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);
    // 默认几种类型文件保存地址
    private static final String DEFAULT_IMAGE_FIR = "image";
    private static final String DEFAULT_AUDIO_FIR = "audio";
    private static final String DEFAULT_VIDEO_FIR = "video";
    private static final String DEFAULT_DOC_FIR = "doc";

    private static final String POINT = ".";
    public static final String[] ALL_IMAGE_SUFFIX = new String[]{"png","jpg","jpeg","gif","tiff"};
    public static final String[] ALL_VIDEO_SUFFIX = new String[]{"mp4","flv","avi","wmv"};
    private static String root;

    /**
     * 初始化根目录
     * @param rootDir
     */
    public static void init(String rootDir) {
        root = rootDir;
    }

    /**
     * 上传图片(默认文件名策略是UUID形式，默认保存到image文件夹下)
     * @param imageFile 图片文件
     * @return 图片保存地址
     * @throws IOException
     */
    public static String uploadImage(MultipartFile imageFile) throws IOException {
        return fileUpload(imageFile, DEFAULT_IMAGE_FIR, FileNameGenerateStrategy.UUID_NAME);
    }

    /**
     * 上传视频(默认文件名策略是UUID形式，默认保存到video文件夹下)
     * @param videoFile 视频文件
     * @return 文件保存地址
     * @throws IOException
     */
    public static String uploadVideo(MultipartFile videoFile) throws IOException {
        return fileUpload(videoFile, DEFAULT_VIDEO_FIR, FileNameGenerateStrategy.UUID_NAME);
    }

    /**
     * 上传音频(默认文件名策略是UUID形式，默认保存到audio文件夹下)
     * @param audioFile 音频文件
     * @return 文件保存地址
     * @throws IOException
     */
    public static String uploadAudio(MultipartFile audioFile) throws IOException {
        return fileUpload(audioFile, DEFAULT_AUDIO_FIR, FileNameGenerateStrategy.UUID_NAME);
    }

    /**
     * 上传文档(默认文件名策略是UUID形式，默认保存到doc文件夹下)
     * @param docFile 文档文件
     * @return 文件保存地址
     * @throws IOException
     */
    public static String uploadDco(MultipartFile docFile) throws IOException {
        return fileUpload(docFile, DEFAULT_DOC_FIR, FileNameGenerateStrategy.UUID_NAME);
    }

    /**
     * 上传文件(文件名不变)
     * @param file 文件
     * @param directory 文件夹(不可直接放到根目录，所以文件夹不能为空)
     * @return 保存的文件地址
     */
    public static String fileUpload(MultipartFile file, @NonNull String directory) throws IOException {
        return fileUpload(file, directory, FileNameGenerateStrategy.SOURCE_FILE_NAME);
    }

    /**
     * 上传文件
     * @param sourceFile 源文件
     * @param directory 文件夹(不可直接放到根目录，所以文件夹不能为空)
     * @param fileNameGenerateStrategy 文件名生成策略
     * @return 保存的文件地址
     */
    public static String fileUpload(MultipartFile sourceFile, @NonNull String directory, @NonNull FileNameGenerateStrategy fileNameGenerateStrategy) throws IOException {
        // 判断文件名生成策略,根据策略生成不同的文件名
        String fileName;
        switch (fileNameGenerateStrategy) {
            case TIMESTAMP_NAME:
                fileName = System.currentTimeMillis() + "";
                break;
            case DATE_NAME:
                fileName = TimeUtil.getTimeStr(TimeUtil.PATTERN_D);
                break;
            case UUID_NAME:
                fileName = UUID.randomUUID().toString();
                break;
            case NEW_NAME:
            case SOURCE_FILE_NAME:
            default:
                fileName = FilenameUtils.removeExtension(sourceFile.getOriginalFilename());
                break;
        }
        if (StringUtil.isEmpty(fileName)) {
            fileName = UUID.randomUUID().toString();
        }
        return fileUpload(sourceFile, directory, fileName);
    }

    /**
     * 上传文件
     * @param sourceFile 源文件
     * @param directory 文件夹(不可直接放到根目录，所以文件夹不能为空)
     * @param fileName 文件名
     * @return 保存的文件地址
     */
    public static String fileUpload(MultipartFile sourceFile, @NonNull String directory, @NonNull String fileName) throws IOException {
        checkRoot();
        if (StringUtil.isEmpty(directory)) {
            LOGGER.info("保存文件夹不能为空");
            throw new CommonException(CommonExceptionEnum.DIRECTORY_CAN_NOT_BE_NULL);
        }
        // 处理文件名，文件名不能包含特殊符号
        fileName = processFileName(fileName);
        if (StringUtil.isEmpty(fileName)) {
            LOGGER.info("文件名不能为空");
            throw new CommonException(CommonExceptionEnum.FILENAME_CAN_NOT_BE_NULL);
        }
        if (Objects.isNull(sourceFile)) {
            LOGGER.info("文件不存在");
            throw new CommonException(CommonExceptionEnum.FILE_NOT_EXIST);
        }
        // 检测保存文件夹是否存在，如果不存在就新建
        String dirPath = getRealPath(directory);
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            boolean mkDirResult = dirFile.mkdirs();
            if (!mkDirResult) {
                LOGGER.info("新建文件夹失败{}", directory);
                throw new CommonException(CommonExceptionEnum.MAKE_DIRECTORY_FAIL);
            }
        }
        // 获取文件后缀
        String fileSuffix = FilenameUtils.getExtension(sourceFile.getOriginalFilename());
        // 保存文件
        String filePath = dirPath + File.separator + fileName + POINT + fileSuffix;
        File file = new File(filePath);
        if (file.exists()) {
            LOGGER.info("同名文件已存在[{}]", filePath);
            throw new CommonException(CommonExceptionEnum.FILE_ALREADY_EXIST);
        }
        // 将IO异常抛出，由上层取处理
        sourceFile.transferTo(file);
        // 返回文件保存地址的时候需要去掉根目录
        String savePath = filePath.replaceFirst(root, "");
        if (!savePath.startsWith(File.separator)) {
            savePath = File.separator + savePath;
        }
        return savePath;
    }

    /**
     * 通过文件名判断是否是图片
     * PS:此判断仅判断文件名后缀，不是严格的判断
     * 严格的判断可以通过ImageIO来判断是否为图片，不过需要多次IO，比较耗费性能，不划算
     * @param fileName
     * @return
     */
    public static boolean isImage(String fileName) {
        return judgeSuffix(fileName, ALL_IMAGE_SUFFIX);
    }

    /**
     * 通过文件名判断是否是视频
     * @param fileName
     * @return
     */
    public static boolean isVideo(String fileName) {
        return judgeSuffix(fileName, ALL_VIDEO_SUFFIX);
    }

    /**
     * 判断文件后缀是否为指定文件
     * @param fileName 文件名
     * @param suffixes 文件后缀列表
     * @return 是否为指定文件类型
     */
    public static boolean judgeSuffix(String fileName, String[] suffixes) {
        if (StringUtil.isEmpty(fileName)) {
            return false;
        }
        String suffix = FilenameUtils.getExtension(fileName);
        if (StringUtil.isEmpty(suffix)) {
            return false;
        }
        for (String imageSuffix : suffixes) {
            if (imageSuffix.equalsIgnoreCase(suffix)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 批量删除文件
     * @param paths 文件相对路径
     * @return 删除成功数量
     */
    public static int deleteFiles(List<String> paths) {
        if (CollectionUtils.isEmpty(paths)) {
            return 0;
        }
        checkRoot();
        int successCount = 0;
        for (String path : paths) {
            path = getRealPath(path);
            File file = new File(path);
            if (file.exists()) {
                boolean success = file.delete();
                if (success) {
                    successCount ++;
                }
            }
        }
        return successCount;
    }

    /**
     * 处理文件名
     * @param fileName
     * @return
     */
    private static String processFileName(String fileName) {
        if (StringUtil.isEmpty(fileName)) {
            return fileName;
        }
        return fileName.replaceAll("\\.", "")
                .replaceAll(File.separator, "");
    }

    /**
     * 获取绝对地址
     * @param path 相对地址
     * @return 绝对地址
     */
    private static String getRealPath(String path) {
        checkRoot();
        if (StringUtil.isEmpty(path) || path.length() <= 1) {
            return "";
        }
        if (path.startsWith(File.separator)) {
            path = path.replaceFirst(File.separator, "");
        }
        if (path.endsWith(File.separator)) {
            path = path.substring(0, path.length() - 1);
        }
        if (!root.endsWith(File.separator)) {
            return root + File.separator + path;
        }
        return root + path;
    }

    /**
     * 检测是否初始化文件系统
     */
    private static void checkRoot() {
        if (StringUtil.isEmpty(root)) {
            LOGGER.info("文件系统未初始化");
            throw new CommonException(CommonExceptionEnum.FILE_SYS_NOT_INIT);
        }
    }
}
