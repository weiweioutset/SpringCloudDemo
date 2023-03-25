package com.cloud.demo.utils;

import eu.bitwalker.useragentutils.UserAgent;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author weiwei
 * @Date 2022/7/3 下午5:15
 * @Version 1.0
 * @Desc 设备工具类，获取设备系统版本，浏览器信息等
 */
public class DeviceUtil {
    private static final String UNKNOWN = "Unknown";

    /**
     * 获取系统
     * @param request
     * @return
     */
    public static String getSystemOS(HttpServletRequest request) {
        if (request == null || StringUtil.isEmpty(request.getHeader("User-Agent"))) {
            return UNKNOWN;
        }
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        return userAgent.getOperatingSystem().getName();
    }

    /**
     * 获取浏览器信息
     * @param request
     * @return
     */
    public static String getBrowserName(HttpServletRequest request) {
        if (request == null || StringUtil.isEmpty(request.getHeader("User-Agent"))) {
            return UNKNOWN;
        }
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        return userAgent.getBrowser().getName();
    }
}
