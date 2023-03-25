package com.cloud.demo.idgenerator.generator;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/6/18 下午6:49
 * @Version 1.0
 * @Desc
 */
public interface IdGenerator {
    /**
     * get next id
     * @return
     */
    Long nextId();

    /**
     * get next id batch
     * @param batchSize
     * @return
     */
    List<Long> nextId(Integer batchSize);
}
