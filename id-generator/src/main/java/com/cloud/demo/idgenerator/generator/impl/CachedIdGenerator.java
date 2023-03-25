package com.cloud.demo.idgenerator.generator.impl;

import com.cloud.demo.idgenerator.entity.Result;
import com.cloud.demo.idgenerator.entity.ResultCode;
import com.cloud.demo.idgenerator.entity.SegmentId;
import com.cloud.demo.idgenerator.generator.IdGenerator;
import com.cloud.demo.idgenerator.service.SegmentIdService;
import com.cloud.demo.idgenerator.utils.NamedThreadFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author weiwei
 * @Date 2022/6/18 下午6:50
 * @Version 1.0
 * @Desc
 */
public class CachedIdGenerator implements IdGenerator {
    protected String bizType;
    protected SegmentIdService segmentIdService;
    protected volatile SegmentId current;
    protected volatile SegmentId next;
    private volatile boolean isLoadingNext;
    private Object lock = new Object();
    private ExecutorService executorService = Executors.newSingleThreadExecutor(new NamedThreadFactory("tinyid-generator"));

    public CachedIdGenerator(String bizType, SegmentIdService segmentIdService) {
        this.bizType = bizType;
        this.segmentIdService = segmentIdService;
        loadCurrent();
    }

    public synchronized void loadCurrent() {
        if (current == null || !current.useful()) {
            if (next == null) {
                SegmentId segmentId = querySegmentId();
                this.current = segmentId;
            } else {
                current = next;
                next = null;
            }
        }
    }

    private SegmentId querySegmentId() {
        String message = null;
        try {
            SegmentId segmentId = segmentIdService.getNextSegmentId(bizType);
            if (segmentId != null) {
                return segmentId;
            }
        } catch (Exception e) {
            message = e.getMessage();
        }
        throw new RuntimeException("error query segmentId: " + message);
    }

    public void loadNext() {
        if (next == null && !isLoadingNext) {
            synchronized (lock) {
                if (next == null && !isLoadingNext) {
                    isLoadingNext = true;
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 无论获取下个segmentId成功与否，都要将isLoadingNext赋值为false
                                next = querySegmentId();
                            } finally {
                                isLoadingNext = false;
                            }
                        }
                    });
                }
            }
        }
    }

    @Override
    public Long nextId() {
        while (true) {
            if (current == null) {
                loadCurrent();
                continue;
            }
            Result result = current.nextId();
            if (result.getCode() == ResultCode.OVER) {
                loadCurrent();
            } else {
                if (result.getCode() == ResultCode.LOADING) {
                    loadNext();
                }
                return result.getId();
            }
        }
    }

    @Override
    public List<Long> nextId(Integer batchSize) {
        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            Long id = nextId();
            ids.add(id);
        }
        return ids;
    }

}
