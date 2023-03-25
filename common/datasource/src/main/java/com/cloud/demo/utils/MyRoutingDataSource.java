package com.cloud.demo.utils;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @Author weiwei
 * @Date 2022/5/12 下午11:03
 * @Version 1.0
 * @Desc
 */
public class MyRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DBContextHolder.get();
    }
}
