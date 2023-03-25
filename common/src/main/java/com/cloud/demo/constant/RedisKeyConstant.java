package com.cloud.demo.constant;

/**
 * @Author weiwei
 * @Date 2022/5/7 下午10:44
 * @Version 1.0
 * @Desc Redis Key以及Redis Key过期时间
 */
public class RedisKeyConstant {
    // 用户信息
    public static final String USER_TOKEN_INFO_KEY = "USER_TOKEN_INFO:";
    // 接口权限
    public static final String RESOURCE_ROLES_MAP = "AUTH:RESOURCE_ROLES_MAP";
    // 权限前缀
    public static final String REDIS_OAUTH_TOKEN_PREFIX = "REDIS_OAUTH_TOKEN_PREFIX:";

    /**
     * 动态-Feed流-时间轴(收件箱)
     */
    public static final String MOMENTS_FEED_STREAM_TIMELINE_KEY = "MOMENTS_FEED_STREAM_TIMELINE_KEY:";
    // 动态-Feed流-发件箱
    public static final String MOMENTS_FEED_STREAM_SEND_KEY = "MOMENTS_FEED_STREAM_SEND_KEY:";

    // AccessToken两小时过期
    public static final int TOKEN_EXPIRE_TIME_SECOND = 2 * 60 * 60;
    // RefreshToken15天过期
    public static final int REFRESH_TOKEN_EXPIRE_TIME_SECOND = 15 * 24 * 60 * 60;

    // GET请求时间间隔 500毫秒
    public static final int GET_REQUEST_DURATION = 500;
    // POST请求时间间隔 2秒
    public static final int POST_REQUEST_DURATION = 2000;
    // 账号注册接口请求时间间隔 10小时
    public static final int REGISTER_REQUEST_DURATION = 10 * 60 * 60 * 1000;
}
