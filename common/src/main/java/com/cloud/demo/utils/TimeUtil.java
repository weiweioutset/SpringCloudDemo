package com.cloud.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/7/16 下午1:22
 * @Version 1.0
 * @Desc 时间处理工具类
 */
public class TimeUtil {
    public static final String PATTERN_A = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_B = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_C = "yyyy-MM-dd";
    public static final String PATTERN_D = "yyyy-MM-dd-HH:mm:ss";
    public static final String PATTERN_E = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * 将时间转换为时分秒的格式
     * @param time 时间(毫秒)
     * @return
     */
    public static String parseTime2HourMinSe(long time) {
        if (time < 1000) {
            return "0时0分0秒";
        }
        int hour = (int) time / 1000 / 60 / 60;
        int minute = (int) time / 1000 / 60 % 60;
        int second = (int) time / 1000 % 60;
        return hour + "时" + minute + "分" + second + "秒";
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getTimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(PATTERN_A);
        return sdf.format(new Date());
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getTimeStr(String pattern) {
        if (StringUtil.isEmpty(pattern)) {
            pattern = PATTERN_A;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date());
    }
}
