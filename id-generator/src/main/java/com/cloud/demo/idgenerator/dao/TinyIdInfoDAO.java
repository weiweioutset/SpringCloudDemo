package com.cloud.demo.idgenerator.dao;

import com.cloud.demo.idgenerator.dao.entity.TinyIdInfo;

/**
 * @Author weiwei
 * @Date 2022/6/18 下午4:39
 * @Version 1.0
 * @Desc
 */
public interface TinyIdInfoDAO {
    /**
     * 根据bizType获取db中的tinyId对象
     * @param bizType
     * @return
     */
    TinyIdInfo queryByBizType(String bizType);

    /**
     * 根据id、oldMaxId、version、bizType更新最新的maxId
     * @param id
     * @param newMaxId
     * @param oldMaxId
     * @param version
     * @param bizType
     * @return
     */
    int updateMaxId(Long id, Long newMaxId, Long oldMaxId, Long version, String bizType);
}
