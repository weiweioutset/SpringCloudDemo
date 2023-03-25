package com.cloud.demo.utils;

import com.cloud.demo.enums.DBTypeEnum;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author weiwei
 * @Date 2022/5/12 下午11:03
 * @Version 1.0
 * @Desc
 */
public class DBContextHolder {
    private static final ThreadLocal<DBTypeEnum> contextHolder = new ThreadLocal<>();
    private static final AtomicInteger counter = new AtomicInteger(-1);

    public static void set(DBTypeEnum dbType) {
        contextHolder.set(dbType);
    }

    public static DBTypeEnum get() {
        return contextHolder.get();
    }

    public static void master() {
        set(DBTypeEnum.MASTER);
    }

    public static void slave() {
        //  轮询，多个从库的情况下随机一个从库读取
        int count = counter.incrementAndGet() % 3;
        switch (count) {
            case 1:
                set(DBTypeEnum.SLAVE_01);
                break;
            case 2:
                set(DBTypeEnum.SLAVE_02);
                break;
            default:
                set(DBTypeEnum.SLAVE_03);
                break;
        }

        // 防止counter过大
        if (counter.get() > 10000) {
            counter.set(-1);
        }
    }

    public static void reset() {
        contextHolder.remove();
    }
}
