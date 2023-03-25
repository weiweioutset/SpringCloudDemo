package com.cloud.demo.service.api;

import com.cloud.demo.vo.UserVo;

/**
 * @Author weiwei
 * @Date 2022/4/22 下午9:27
 * @Version 1.0
 * @Desc 用户注册服务
 */
public interface IUserRegisterService {
    /**
     * 账号注册
     * @param userVo
     * @return
     */
    UserVo register(UserVo userVo);
}
