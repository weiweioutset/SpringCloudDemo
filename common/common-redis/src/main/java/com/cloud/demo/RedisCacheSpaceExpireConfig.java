package com.cloud.demo;

import java.util.HashMap;

/**
 * @Author weiwei
 * @Date 2022/5/15 下午4:14
 * @Version 1.0
 * @Desc
 */
public class RedisCacheSpaceExpireConfig {
    public static final String MINUTE_1 = "MINUTE_1";
    public static final String MINUTE_3 = "MINUTE_3";
    public static final String MINUTE_5 = "MINUTE_5";
    public static final String MINUTE_15 = "MINUTE_10";
    public static final String MINUTE_30 = "MINUTE_30";

    public static final String HOUR_1 = "HOUR_1";
    public static final String HOUR_2 = "HOUR_2";
    public static final String HOUR_6 = "HOUR_6";
    public static final String HOUR_12 = "HOUR_12";

    public static final String DAY_1 = "DAY_1";
    public static final String DAY_3 = "DAY_3";
    public static final String DAY_10 = "DAY_10";
    public static final String DAY_30 = "DAY_30";

    /**
     * 过期时间键值对 k-v : 名称-过期时间(分钟)
     */
    public static final HashMap<String, Integer> EXPIRE_TIME_MAP = new HashMap<String, Integer>(){
        {
            put(MINUTE_1, 1);
            put(MINUTE_3, 3);
            put(MINUTE_5, 5);
            put(MINUTE_15, 15);
            put(MINUTE_30, 30);

            put(HOUR_1, 60);
            put(HOUR_2, 2 * 60);
            put(HOUR_6, 6 * 60);
            put(HOUR_12, 12 * 60);

            put(DAY_1, 60 * 24);
            put(DAY_3, 60 * 24 * 3);
            put(DAY_10, 60 * 24 * 10);
            put(DAY_30, 60 * 24 * 30);

        }
    };
}
