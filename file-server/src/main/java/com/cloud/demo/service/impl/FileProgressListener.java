package com.cloud.demo.service.impl;

import com.cloud.demo.constant.RedisKeyConstant;
import com.cloud.demo.utils.RedisUtils;
import com.cloud.demo.vo.CommonProgressVo;
import com.qcloud.cos.event.ProgressEvent;
import com.qcloud.cos.event.ProgressEventType;
import com.qcloud.cos.event.ProgressListener;
import com.qcloud.cos.transfer.Upload;

import java.util.concurrent.TimeUnit;

/**
 * @Author weiwei
 * @Date 2022/8/20 下午1:52
 * @Version 1.0
 * @Desc
 */
public class FileProgressListener implements ProgressListener {
    private final RedisUtils redisUtils;
    private final String key;
    private final CommonProgressVo progressVo;
    private Upload upload;

    private long bytesWritten = 0;
    private final long totalBytes;

    public FileProgressListener(String key, RedisUtils redisUtils, long totalLength) {
        this.redisUtils = redisUtils;
        this.key = key;
        this.progressVo = new CommonProgressVo(key);
        this.totalBytes = totalLength;
        progressVo.setTotalByte(totalLength);
    }

    public void setUpload(Upload upload) {
        this.upload = upload;
    }

    @Override
    public void progressChanged(ProgressEvent progressEvent) {
        long bytes = progressEvent.getBytes();
        ProgressEventType eventType = progressEvent.getEventType();
        switch (eventType) {
            case TRANSFER_PREPARING_EVENT:
                progressVo.setState("PREPARING");
                break;
            case TRANSFER_STARTED_EVENT:
                progressVo.setState("STARTED");
                break;
            case REQUEST_BYTE_TRANSFER_EVENT:
                this.bytesWritten += bytes;
                double percent = this.bytesWritten * 100.00F / this.totalBytes;
                if (percent >= 100.00F) {
                    percent = 99.99F;
                }
                if (percent >= 50 && upload != null) {
                    System.out.println("取消上传");
                    upload.abort();
                }
                progressVo.setPercent(percent);
                // 因为一些原因，实际bytesWritten可能要比totalBytes要大
                if (bytesWritten > totalBytes) {
                    bytesWritten = totalBytes;
                }
                progressVo.setSofarByte(bytesWritten);
                break;
            case TRANSFER_COMPLETED_EVENT:
                progressVo.setState("COMPLETED");
                progressVo.setPercent(100F);
                progressVo.setSuccess(true);
                break;
            case TRANSFER_CANCELED_EVENT:
                progressVo.setState("CANCELED");
                break;
            case TRANSFER_FAILED_EVENT:
                progressVo.setState("FAILED");
                progressVo.setSuccess(false);
                break;
            default:
                break;
        }

        redisUtils.setValue(RedisKeyConstant.FILE_PROGRESS_KEY + key, progressVo, 1L, TimeUnit.DAYS);
    }
}
