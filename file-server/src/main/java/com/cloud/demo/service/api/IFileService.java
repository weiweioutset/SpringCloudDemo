package com.cloud.demo.service.api;

import com.cloud.demo.vo.CommonProgressVo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @Author weiwei
 * @Date 2022/8/16 下午11:21
 * @Version 1.0
 * @Desc
 */
public interface IFileService {

    /**
     * 上传文件
     * @param file
     * @return
     */
    String upload(MultipartFile file);

    /**
     * 分块上传文件
     * @param file
     * @return
     */
    String blockUpload(MultipartFile file) throws IOException;

    /**
     * 获取上传/下载进度
     */
    CommonProgressVo uploadProgress(String uploadId);

    void cancelUpload(String uploadId);

    void pauseUpload(String uploadId);

    void resumeUpload(String uploadId);
}
