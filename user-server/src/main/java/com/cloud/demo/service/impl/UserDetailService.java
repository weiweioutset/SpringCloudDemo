package com.cloud.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.exception.CommonException;
import com.cloud.demo.po.User;
import com.cloud.demo.mapper.UserMapper;
import com.cloud.demo.service.api.IUserDetailService;
import com.cloud.demo.service.api.IUserRoleService;
import com.cloud.demo.utils.SecurityUtils;
import com.cloud.demo.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author weiwei
 * @Date 2022/4/22 下午9:28
 * @Version 1.0
 * @Desc 用户详情服务
 */
@Service
public class UserDetailService extends ServiceImpl<UserMapper, User> implements IUserDetailService {
    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public UserVo getByAccountId(Long accountId) {
        // 获取基本信息
        User user = this.getOne(new QueryWrapper<User>()
                .eq("account_id", accountId));
        if (Objects.isNull(user)) {
            throw new CommonException(CommonExceptionEnum.ACCOUNT_NOT_EXIST);
        }
        // 获取角色列表
        return setUserRoles(user);
    }

    @Override
    public UserVo getUserForAuth(String accountId) {
        // 获取基本信息
        User user = this.getOne(new QueryWrapper<User>()
                .eq("account_id", accountId));
        if (Objects.isNull(user)) {
            throw new CommonException(CommonExceptionEnum.ACCOUNT_NOT_EXIST);
        }
        // 获取角色列表
        UserVo userVo = setUserRoles(user);
        if (Objects.isNull(userVo)) {
            throw new CommonException(CommonExceptionEnum.ACCOUNT_NOT_EXIST);
        }
        userVo.setPassword(user.getPassword());
        return userVo;
    }

    @Override
    public UserVo getByPhone(String phone) {
        User user = this.getOne(new QueryWrapper<User>()
                .eq("phone", phone));
        if (Objects.isNull(user)) {
            throw new CommonException(CommonExceptionEnum.ACCOUNT_NOT_EXIST);
        }
        return setUserRoles(user);
    }

    /**
     * 设置用户的角色
     * @param user
     * @return
     */
    private UserVo setUserRoles(User user) {
        if (Objects.isNull(user)) {
            return null;
        }
        // 获取角色列表
        List<String> roles = userRoleService.getAllUserRoles(user.getAccountId());
        UserVo userVo = new UserVo(user);
        userVo.setRoles(roles);
        return userVo;
    }
}
