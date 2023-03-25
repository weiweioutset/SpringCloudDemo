package com.cloud.demo.utils;

import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.exception.CommonException;
import org.springframework.util.Assert;

/**
 * @Author weiwei
 * @Date 2022/5/7 下午10:55
 * @Version 1.0
 * @Desc
 */
public class AssertUtils {
    private static final CommonException INVALID_PARAMS = new CommonException(CommonExceptionEnum.INVALID_PARAMS);

    /**
     * 判断参数是否为null
     * @param object
     */
    public static void notNull(Object object) {
        Assert.notNull(object, INVALID_PARAMS.getMessage());
    }

    /**
     * 判断参数是否为null
     * @param objects
     */
    public static void notNull(Object...objects) {
        for (Object object : objects) {
            Assert.notNull(object, INVALID_PARAMS.getMessage());
        }
    }

    /**
     * 判断参数是否为null
     * @param object
     * @param exceptionEnum
     */
    public static void notNull(Object object, CommonExceptionEnum exceptionEnum) {
        Assert.notNull(object, exceptionEnum.getMessage());
    }
}
