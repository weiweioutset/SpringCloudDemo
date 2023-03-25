package com.cloud.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.demo.mapper.UserLoginLogMapper;
import com.cloud.demo.po.UserLoginLog;
import com.cloud.demo.service.api.IUserLoginLogService;
import com.cloud.demo.vo.UserLoginLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author weiwei
 * @Date 2022/6/30 下午10:52
 * @Version 1.0
 * @Desc 用户登录日志服务
 */
@Service
public class UserLoginLogService extends ServiceImpl<UserLoginLogMapper, UserLoginLog> implements IUserLoginLogService {
    @Autowired
    private UserLoginLogMapper loginLogMapper;

    @Override
    public UserLoginLogVo getUserNewestLogin(Long accountId, String clientType) {
        UserLoginLog loginLog = loginLogMapper.getNewest(accountId, clientType);
        return new UserLoginLogVo(loginLog);
    }

    @Override
    public boolean insert(UserLoginLogVo loginLogVo) {
        UserLoginLog loginLog = new UserLoginLog(loginLogVo);
        return this.save(loginLog);
    }
}
