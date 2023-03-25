package com.cloud.demo.idgenerator.service;

import com.cloud.demo.idgenerator.entity.SegmentId;

/**
 * @Author weiwei
 * @Date 2022/6/18 下午6:53
 * @Version 1.0
 * @Desc
 */
public interface SegmentIdService {

    /**
     * 根据bizType获取下一个SegmentId对象
     * @param bizType
     * @return
     */
    SegmentId getNextSegmentId(String bizType);

}
