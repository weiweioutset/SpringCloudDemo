package com.cloud.demo;

import com.cloud.demo.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author weiwei
 * @Date 2022/8/8 下午11:11
 * @Version 1.0
 * @Desc
 */
@SpringBootTest(classes = {MomentsServerApplication.class})
public class RedisTestApp {
    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void testAdd() {
        redisUtils.zAdd("ZSET_TEST_KEY", 1,1);

        redisUtils.zAdd("ZSET_TEST_KEY", 2,2);

        redisUtils.zAdd("ZSET_TEST_KEY", 100,100);

        redisUtils.zAdd("ZSET_TEST_KEY", 200,200);

        redisUtils.zAdd("ZSET_TEST_KEY", 3,3);

    }

    @Test
    public void testGet() {
        Set<Object> set = redisUtils.zRange("ZSET_TEST_KEY", 3, 5);

        System.out.println(set);
    }
}
