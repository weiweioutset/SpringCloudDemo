package com.cloud.demo.service.api;

import com.cloud.demo.vo.UserLoginLogVo;

/**
 * @Author weiwei
 * @Date 2022/6/30 下午10:52
 * @Version 1.0
 * @Desc 用户登录日志服务
 */
public interface IUserLoginLogService {

    /**
     * 获取账号最新一次登录记录
     * @param accountId
     * @return
     */
    UserLoginLogVo getUserNewestLogin(Long accountId, String clientType);

    /**
     * 插入登录记录
     * @param loginLogVo
     * @return
     */
    boolean insert(UserLoginLogVo loginLogVo);
}
