package com.cloud.demo.config;

import com.cloud.demo.enums.DBTypeEnum;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.cloud.demo.utils.MyRoutingDataSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author weiwei
 * @Date 2022/5/12 下午11:01
 * @Version 1.0
 * @Desc
 */
@Configuration
@RefreshScope
@EnableConfigurationProperties
public class DataSourceConfig {
    /**
     * 主数据源
     *
     * @return 返回数据源对象
     */
    @Bean(name = "masterDataSource")
    @RefreshScope
    @ConfigurationProperties(prefix = "spring.druid.master")
    public DataSource writeDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 从数据源
     *
     * @return 返回数据源对象
     */
    @Bean(name = "slaveDataSource01")
    @RefreshScope
    @ConfigurationProperties(prefix = "spring.druid.slave01")
    public DataSource readDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 从数据源
     *
     * @return 返回数据源对象
     */
    @Bean(name = "slaveDataSource02")
    @RefreshScope
    @ConfigurationProperties(prefix = "spring.druid.slave02")
    public DataSource readDataSource01() {
        return DataSourceBuilder.create().build();
    }

    /**
     * 从数据源
     *
     * @return 返回数据源对象
     */
    @Bean(name = "slaveDataSource03")
    @RefreshScope
    @ConfigurationProperties(prefix = "spring.druid.slave03")
    public DataSource readDataSource02() {
        return DataSourceBuilder.create().build();
    }



    @Bean
    @RefreshScope
    @Primary
    public DataSource myRoutingDataSource(
            @Qualifier("masterDataSource") DataSource masterDataSource,
            @Qualifier("slaveDataSource01") DataSource slaveDataSource01,
            @Qualifier("slaveDataSource02") DataSource slaveDataSource02,
            @Qualifier("slaveDataSource03") DataSource slaveDataSource03) {
        Map<Object, Object> targetDataSources = new HashMap<>(4);
        // 添加目标数据源
        targetDataSources.put(DBTypeEnum.MASTER, masterDataSource);
        // 添加多个从库源
        targetDataSources.put(DBTypeEnum.SLAVE_01, slaveDataSource01);
        targetDataSources.put(DBTypeEnum.SLAVE_02, slaveDataSource02);
        targetDataSources.put(DBTypeEnum.SLAVE_03, slaveDataSource03);
        MyRoutingDataSource myRoutingDataSource = new MyRoutingDataSource();
        // 设置默认数据源
        myRoutingDataSource.setDefaultTargetDataSource(masterDataSource);
        // 配置目标数据源
        myRoutingDataSource.setTargetDataSources(targetDataSources);
        return myRoutingDataSource;
    }
}
