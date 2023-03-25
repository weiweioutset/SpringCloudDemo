package com.cloud.demo.service.api;

import com.cloud.demo.po.User;
import com.cloud.demo.vo.UserVo;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/4/22 下午9:27
 * @Version 1.0
 * @Desc 用户详情服务
 */
public interface IUserDetailService {
    /**
     * 根据账号id获取用户信息
     * @param accountId
     * @return
     */
    UserVo getByAccountId(Long accountId);

    /**
     * 根据账号id获取用户信息，带有密码，仅授权服务使用
     * @param accountId
     * @return
     */
    UserVo getUserForAuth(String accountId);

    /**
     * 根据手机号获取用户信息
     * @param phone
     * @return
     */
    UserVo getByPhone(String phone);
}
