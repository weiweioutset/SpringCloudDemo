package com.cloud.demo.utils;

import com.cloud.demo.exception.CommonException;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.InitiateMultipartUploadRequest;
import com.qcloud.cos.model.InitiateMultipartUploadResult;
import com.qcloud.cos.model.PartETag;
import com.qcloud.cos.model.UploadPartRequest;
import com.qcloud.cos.region.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author weiwei
 * @Date 2022/9/4 下午9:22
 * @Version 1.0
 * @Desc 分块上传工具类
 */
@Component
public class BlockUploadUtil {
    private final Logger LOGGER = LoggerFactory.getLogger(BlockUploadUtil.class);
    private static final String secretId = "AKIDmMgjUyC9pENFq9CyUifMmulMgjV1FCsG";
    private static final String secretKey = "9JDpAE0VnoY1C6k6B0ZrysWu1rRkmX2F";
    private final COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
    // 2 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
    // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
    private final Region region = new Region("ap-guangzhou");
    private final ClientConfig clientConfig = new ClientConfig(region);
    // 3 生成 cos 客户端。
    private final COSClient cosClient = new COSClient(cred, clientConfig);
    // 存储桶
    String bucket = "bucket01-1257037728"; //存储桶名称，格式：BucketName-APPID

    public String upload(File file, String key) {
        return upload(file, key, createDefaultExecutorService());
    }

    public String upload(File file, String key, ExecutorService threadPool) {
        /**1.初始化一个分片上传事件**/
        InitiateMultipartUploadRequest request=new InitiateMultipartUploadRequest(bucket, key);
        InitiateMultipartUploadResult result = cosClient.initiateMultipartUpload(request);

        UploadPartRequest partRequest = new UploadPartRequest();
        partRequest.setBucketName(bucket);
        partRequest.setUploadId(result.getUploadId());
        partRequest.setKey(key);
        List<PartETag> partETags = new CopyOnWriteArrayList<>();

        try {
            /**2.上传分片**/
            //计算文件有多少个分片
            long partSize= 500 * 1024L;
            long fileLength = file.length();
            int partCount = (int) (fileLength / partSize);
            if (fileLength % partSize != 0) {
                partCount ++;
            }
            if (partCount > 10000) {
                throw new CommonException("Total parts count should not exceed 10000");
            }
            // 遍历分片上传
            for (int i = 0; i < partCount; i++) {
                long startPos = i * partSize;
                //是否为最后一块分片
                long curPartSize = (i + 1 == partCount) ? (fileLength-startPos) : partSize;
                partRequest.setPartSize(curPartSize);
                threadPool.execute(new PartUploader(cosClient,file,startPos,partRequest,partETags,i+1));
            }
            //等待所有的分片完成
            threadPool.shutdown();

        } catch (CosClientException clientException) {

        }
        return null;
    }



    /**
     * 创建一个默认的单线程线程池
     * @return
     */
    private static ThreadPoolExecutor createDefaultExecutorService() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private int threadCount = 1;

            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("block-upload-worker-" + threadCount++);
                return thread;
            }
        };
        return (ThreadPoolExecutor) Executors.newFixedThreadPool(1, threadFactory);
    }
}
