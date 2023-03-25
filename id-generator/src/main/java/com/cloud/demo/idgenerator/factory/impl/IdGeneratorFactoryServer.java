package com.cloud.demo.idgenerator.factory.impl;

import com.cloud.demo.idgenerator.factory.AbstractIdGeneratorFactory;
import com.cloud.demo.idgenerator.generator.IdGenerator;
import com.cloud.demo.idgenerator.generator.impl.CachedIdGenerator;
import com.cloud.demo.idgenerator.service.SegmentIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author weiwei
 * @Date 2022/6/18 下午6:57
 * @Version 1.0
 * @Desc
 */
@Component
public class IdGeneratorFactoryServer extends AbstractIdGeneratorFactory {

    private static final Logger logger = LoggerFactory.getLogger(CachedIdGenerator.class);
    @Autowired
    private SegmentIdService tinyIdService;

    @Override
    public IdGenerator createIdGenerator(String bizType) {
        logger.info("createIdGenerator :{}", bizType);
        return new CachedIdGenerator(bizType, tinyIdService);
    }
}
