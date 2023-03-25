package com.cloud.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloud.demo.constant.RedisKeyConstant;
import com.cloud.demo.exception.CommonException;
import com.cloud.demo.service.api.IFileService;
import com.cloud.demo.utils.RedisUtils;
import com.cloud.demo.utils.StringUtil;
import com.cloud.demo.vo.CommonProgressVo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.event.ProgressEvent;
import com.qcloud.cos.event.ProgressListener;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * @Author weiwei
 * @Date 2022/8/16 下午11:21
 * @Version 1.0
 * @Desc
 */
@Service
public class FileService implements IFileService {
    private Logger logger = LoggerFactory.getLogger(FileService.class);
    private static final String secretId = "AKIDmMgjUyC9pENFq9CyUifMmulMgjV1FCsG";
    private static final String secretKey = "9JDpAE0VnoY1C6k6B0ZrysWu1rRkmX2F";
    private final COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
    // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
    // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
    private final Region region = new Region("ap-guangzhou");
    private final ClientConfig clientConfig = new ClientConfig(region);
    // 3 生成 cos 客户端。
    private final COSClient cosClient = new COSClient(cred, clientConfig);
    private TransferManager transferManager = null;
    // 存储桶
    String bucket = "bucket01-1257037728"; //存储桶名称，格式：BucketName-APPID
    private static final String FILE_PATH_PREFIX = "\\";
    private static final String FILE_PREFIX = "/";
    private ConcurrentHashMap<String, Upload> uploadTasks = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, PersistableUpload> pauseTask = new ConcurrentHashMap<>();
    @Autowired
    private RedisUtils redisUtils;

    @PostConstruct
    public void init() {
        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        transferManager = new TransferManager(cosClient);

        // 设置高级接口的配置项
        // 分块上传阈值和分块大小分别为 5MB 和 1MB
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(2*1024*1024);
        transferManagerConfiguration.setMinimumUploadPartSize(1024*1024);
        transferManager.setConfiguration(transferManagerConfiguration);
    }

    @Override
    public String upload(MultipartFile file) {
        //判断文件不为空
        if (Objects.isNull(file) || file.getSize() <= 0) {
            throw new CommonException("未指定文件!");
        }
        File localFile = null;
        String originalFilename = null;
        String fileSuffix,filePrefix;
        try {
            originalFilename = file.getOriginalFilename();
            fileSuffix = FilenameUtils.getExtension(originalFilename);
            filePrefix = FilenameUtils.removeExtension(originalFilename);
            if (StringUtil.isEmpty(filePrefix) || StringUtil.isEmpty(fileSuffix)) {
                throw new CommonException("上传失败!");
            }
            logger.info("fileName = {}",originalFilename);
            localFile = File.createTempFile(filePrefix, fileSuffix);
            //将localFile这个文件所指向的文件  上传到对应的目录
            file.transferTo(localFile);
            localFile.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("MultipartFile transfer file IOException ={}",e.getMessage());
            //文件上传失败就返回错误响应
            throw new CommonException("上传失败!");
        }
        if(localFile == null){
            throw new CommonException("临时文件为空！");
        }
        //2.【将文件上传到腾讯云】
        // PutObjectRequest(参数1,参数2,参数3)参数1:存储桶,参数2:指定腾讯云的上传文件路径,参数3:要上传的文件
        String key = "file" + "/" + UUID.randomUUID() + "." +fileSuffix;
        //String key = baseUrl + "/" + originalFilename;
        logger.info("key = {}", key);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, localFile);
        //设置存储类型 默认标准型
        putObjectRequest.setStorageClass(StorageClass.Standard);
        //获得到客户端
        try {
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            //putObjectResult 会返回etag
            String eTag = putObjectResult.getETag();
            logger.info("eTag = {}", eTag);
        } catch (CosServiceException e) {
            logger.error("CosServiceException ={}", e.getMessage());
            throw new CosServiceException(e.getMessage());
        } catch (CosClientException e) {
            logger.error("CosClientException ={}", e.getMessage());
            throw new CosClientException(e.getMessage());
        }
        cosClient.shutdown();

        String url = "https" + "/" + key;
        logger.info("上传路径:"+url);
        return url;
    }

    @Override
    public String blockUpload(MultipartFile file) throws IOException {
        File localFile = null;
        String originalFilename = null;
        String fileSuffix,filePrefix;
        try {
            originalFilename = file.getOriginalFilename();
            fileSuffix = FilenameUtils.getExtension(originalFilename);
            filePrefix = FilenameUtils.removeExtension(originalFilename);
            if (StringUtil.isEmpty(filePrefix) || StringUtil.isEmpty(fileSuffix)) {
                throw new CommonException("上传失败!");
            }
            logger.info("fileName = {}",originalFilename);
            localFile = File.createTempFile(filePrefix, fileSuffix);
            //将localFile这个文件所指向的文件  上传到对应的目录
            file.transferTo(localFile);
            localFile.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("MultipartFile transfer file IOException ={}",e.getMessage());
            //文件上传失败就返回错误响应
            throw new CommonException("上传失败!");
        }

        String fileNameKey = UUID.randomUUID().toString().replaceAll("-", "");
        String key = "file" + "/" + fileNameKey + "." + fileSuffix;
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 上传的流如果能够获取准确的流长度，则推荐一定填写 content-length
        // 如果确实没办法获取到，则下面这行可以省略，但同时高级接口也没办法使用分块上传了
        objectMetadata.setContentLength(localFile.length());
        logger.info("key = {}, md5 = {}", key, fileNameKey);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, localFile);
        //设置存储类型 默认标准型
        putObjectRequest.setStorageClass(StorageClass.Standard);
        putObjectRequest.setTrafficLimit(819200);
        FileProgressListener progressListener = new FileProgressListener(fileNameKey, redisUtils, localFile.length());
        putObjectRequest.setGeneralProgressListener(progressListener);
        try {
            // 异步上传任务
            Upload upload = transferManager.upload(putObjectRequest);
            uploadTasks.put(fileNameKey, upload);
            return fileNameKey;
        } catch (Exception e) {
            logger.error("上传文件错误", e);
            return null;
        }
    }

    @Override
    public CommonProgressVo uploadProgress(String uploadId) {
        if (StringUtil.isEmpty(uploadId)) {
            return null;
        }
        String key = RedisKeyConstant.FILE_PROGRESS_KEY + uploadId;
        return  (CommonProgressVo) redisUtils.getValue(key);
    }

    @Override
    public void cancelUpload(String uploadId) {

    }

    @Override
    public void pauseUpload(String uploadId) {
        Upload upload = uploadTasks.get(uploadId);
        if (upload != null) {
            logger.info("开始暂停{}", uploadId);
            PersistableUpload persistableUpload = upload.pause();
            if (persistableUpload != null) {
                logger.info("暂停{}成功", uploadId);
                pauseTask.put(uploadId, persistableUpload);
            }
        }
    }

    @Override
    public void resumeUpload(String uploadId) {
        PersistableUpload persistableUpload = pauseTask.get(uploadId);
        if (persistableUpload != null) {
            logger.info("开始恢复{}", uploadId);
            transferManager.resumeUpload(persistableUpload);
        }
    }

    /**
     * 获取分块上传的 uploadId
     *
     * @param key
     * @return
     */
    public String InitMultipartUploadDemo(String key) {
        // 初始化分块上传任务
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucket, key);
        // 设置存储类型, 默认是标准(Standard), 低频(Standard_IA), 归档(Archive)
        request.setStorageClass(StorageClass.Standard);
        try {
            InitiateMultipartUploadResult initResult = cosClient.initiateMultipartUpload(request);
            // 获取uploadid
            return initResult.getUploadId();
        } catch (CosClientException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 获取上传的所有分块信息
     *
     * @param uploadId
     * @param key
     * @param file
     * @return
     * @throws IOException
     */
    public List<PartETag> UploadPartDemo(String uploadId, String key, MultipartFile file) throws IOException {
        // 是否进行流量控制
        boolean userTrafficLimit = true;
        // 所有分块
        List<PartETag> partETags = new LinkedList<>();
        // 每片分块的大小 1M
        int partSize = 1024 * 1024;
        // 上传文件的大小
        long fileSize = file.getSize();
        // 分块片数
        int partCount = (int) (fileSize / partSize);
        if (fileSize % partSize != 0) {
            partCount++;
        }
        logger.info("分快上传：fileSize = [{}], partCount = [{}]", fileSize, partCount);
        // 生成要上传的数据, 这里初始化一个10M的数据
        for (int i = 0; i < partCount; i++) {
            // 起始位置
            long startPos = (long) i * partSize;
            // 分片大小
            long curPartSize = (i + 1 == partCount) ? (fileSize - startPos) : partSize;
            logger.info("开始上传第[{}]块，起始位置[{}], 分片大小[{}]", (i+1), startPos, curPartSize);
            // 大文件
            InputStream instream = file.getInputStream();
            // 跳过已经上传的分片。
            instream.skip(startPos);
            // 用于上传分块请求
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            // 存储桶命名
            uploadPartRequest.setBucketName(bucket);
            // 指定分块上传到 COS 上的路径
            uploadPartRequest.setKey(key);
            // 标识指定分块上传的 uploadId
            uploadPartRequest.setUploadId(uploadId);
            // 设置分块的数据来源输入流
            uploadPartRequest.setInputStream(instream);
            // 设置分块的长度
            uploadPartRequest.setPartSize(curPartSize);
            // 上传分片编号
            uploadPartRequest.setPartNumber(i + 1);
            // 是否进行流量控制
            if (userTrafficLimit) {
                // 用于对上传对象进行流量控制,单位：bit/s，默认不进行流量控制
                uploadPartRequest.setTrafficLimit(819200);
            }
            try {
                // 上传分片数据的输入流
                UploadPartResult uploadPartResult = cosClient.uploadPart(uploadPartRequest);
                // 获取上传分块信息
                PartETag partETag = uploadPartResult.getPartETag();
                // 把上传返回的分块信息放入到分片集合中
                partETags.add(partETag);
                // 获取复制生成对象的CRC64
                String crc64 = uploadPartResult.getCrc64Ecma();
                logger.info("上传第[{}]块，crc64 = [{}]", (i+1), crc64);
            } catch (CosServiceException e) {
                throw e;
            } catch (CosClientException e) {
                throw e;
            }
        }
        return partETags;
    }

    /**
     * 完成分片上传
     *
     * @param uploadId
     * @param partETags
     * @param key
     */
    public void completePartDemo(String uploadId, List<PartETag> partETags, String key) {
        // 分片上传结束后，调用complete完成分片上传
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucket, key, uploadId, partETags);
        try {
            CompleteMultipartUploadResult completeResult = cosClient.completeMultipartUpload(completeMultipartUploadRequest);
            String etag = completeResult.getETag();
            String crc64 = completeResult.getCrc64Ecma();
            logger.info("完成分片上传,etag:[{}], crc64:[{}]", etag, crc64);
        } catch (CosServiceException e) {
            throw e;
        } catch (CosClientException e) {
            throw e;
        }
        cosClient.shutdown();
    }

//    private TransferManager createTransferManager() {
//        // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
//        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
////        ExecutorService threadPool = Executors.newFixedThreadPool(32);
//
//        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
//        TransferManager transferManager = new TransferManager(cosClient);
//
//        // 设置高级接口的配置项
//        // 分块上传阈值和分块大小分别为 5MB 和 1MB
//        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
//        transferManagerConfiguration.setMultipartUploadThreshold(2*1024*1024);
//        transferManagerConfiguration.setMinimumUploadPartSize(1024*1024);
//        transferManager.setConfiguration(transferManagerConfiguration);
//
//        return transferManager;
//    }

    private void showTransferProgress(Transfer transfer) {
        // 这里的 Transfer 是异步上传结果 Upload 的父类
        System.out.println(transfer.getDescription());
        // transfer.isDone() 查询上传是否已经完成
        while (!transfer.isDone()) {
            try {
                // 每 2 秒获取一次进度
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return;
            }

            TransferProgress progress = transfer.getProgress();
            long sofar = progress.getBytesTransferred();
            long total = progress.getTotalBytesToTransfer();
            double pct = progress.getPercentTransferred();
            System.out.printf("upload progress: [%d / %d] = %.02f%%\n", sofar, total, pct);
        }

        // 完成了 Completed，或者失败了 Failed
        System.out.println(transfer.getState());
    }

    private void shutdownTransferManager(TransferManager transferManager) {
        // 指定参数为 true, 则同时会关闭 transferManager 内部的 COSClient 实例。
        // 指定参数为 false, 则不会关闭 transferManager 内部的 COSClient 实例。
        transferManager.shutdownNow(true);
    }
}
