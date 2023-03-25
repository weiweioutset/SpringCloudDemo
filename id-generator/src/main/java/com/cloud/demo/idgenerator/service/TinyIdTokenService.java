package com.cloud.demo.idgenerator.service;

/**
 * @Author weiwei
 * @Date 2022/6/18 下午7:00
 * @Version 1.0
 * @Desc
 */
public interface TinyIdTokenService {
    /**
     * 是否有权限
     * @param bizType
     * @param token
     * @return
     */
    boolean canVisit(String bizType, String token);
}
