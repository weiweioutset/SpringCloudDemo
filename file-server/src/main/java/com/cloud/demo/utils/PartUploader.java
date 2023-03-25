package com.cloud.demo.utils;

import com.cloud.demo.service.impl.FileProgressListener;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.event.ProgressEvent;
import com.qcloud.cos.event.ProgressEventType;
import com.qcloud.cos.model.PartETag;
import com.qcloud.cos.model.UploadPartRequest;
import com.qcloud.cos.model.UploadPartResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * @Author weiwei
 * @Date 2022/9/4 下午9:33
 * @Version 1.0
 * @Desc 实现分块上传线程
 */
public class PartUploader implements Runnable {
    private final Logger LOGGER = LoggerFactory.getLogger(PartUploader.class);
    private File localFile;
    private long startPos;
    private final List<PartETag> partETags;
    private UploadPartRequest partRequest;
    private int partNumber = 0;
    /**
     * 进度监听
     */
    private FileProgressListener progressListener;
    /**
     * 客户端
     */
    private final COSClient client;

    public PartUploader(COSClient client, File localFile, long startPos, UploadPartRequest partRequest, List<PartETag> partETags, int partNumber) {
        // 参数判断
        if (startPos < 0 || Objects.isNull(partRequest)
                || StringUtil.isEmpty(partRequest.getBucketName())
                || StringUtil.isEmpty(partRequest.getUploadId())
                || StringUtil.isEmpty(partRequest.getKey())
                || partRequest.getPartSize() <= 0) {
            throw new IllegalArgumentException();
        }
        this.client = client;
        this.localFile = localFile;
        this.startPos = startPos;
        this.partETags = partETags;
        this.partRequest = partRequest;
        this.partNumber = partNumber;
    }

    /**
     * 设置进度监听器
     * @param progressListener
     */
    public void addProgressListener(FileProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(this.localFile);
            // 跳过已经上传的分片
            inputStream.skip(startPos);
            // 开始分块上传
            partRequest.setInputStream(inputStream);
            // 设置分块序号
            partRequest.setPartNumber(partNumber);
            // 设置进度监听
            if (progressListener != null) {
                ProgressEvent event = new ProgressEvent(ProgressEventType.REQUEST_BYTE_TRANSFER_EVENT, partRequest.getPartSize());
                progressListener.progressChanged(event);
            }
            // 开始上传分片
            UploadPartResult uploadPartResult = client.uploadPart(partRequest);
            //每次上传分片之后，OSS的返回结果会包含一个PartETag。PartETag将被保存到PartETags中。
            synchronized (this.partETags) {
                this.partETags.add(uploadPartResult.getPartETag());
            }
        } catch (Exception e) {
            LOGGER.error("upload part error", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("upload part error", e);
                }
            }
        }
    }
}
