package com.cloud.demo.idgenerator.factory;

import com.cloud.demo.idgenerator.generator.IdGenerator;

/**
 * @Author weiwei
 * @Date 2022/6/18 下午6:48
 * @Version 1.0
 * @Desc
 */
public interface IdGeneratorFactory {
    /**
     * 根据bizType创建id生成器
     * @param bizType
     * @return
     */
    IdGenerator getIdGenerator(String bizType);
}
