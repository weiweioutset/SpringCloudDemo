package com.cloud.demo.idgenerator.factory;

import com.cloud.demo.idgenerator.generator.IdGenerator;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author weiwei
 * @Date 2022/6/18 下午6:52
 * @Version 1.0
 * @Desc
 */
public abstract class AbstractIdGeneratorFactory implements IdGeneratorFactory {

    private static ConcurrentHashMap<String, IdGenerator> generators = new ConcurrentHashMap<>();

    @Override
    public IdGenerator getIdGenerator(String bizType) {
        if (generators.containsKey(bizType)) {
            return generators.get(bizType);
        }
        synchronized (this) {
            if (generators.containsKey(bizType)) {
                return generators.get(bizType);
            }
            IdGenerator idGenerator = createIdGenerator(bizType);
            generators.put(bizType, idGenerator);
            return idGenerator;
        }
    }

    /**
     * 根据bizType创建id生成器
     *
     * @param bizType
     * @return
     */
    protected abstract IdGenerator createIdGenerator(String bizType);
}
