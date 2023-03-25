package com.cloud.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.demo.mapper.UserRoleMapper;
import com.cloud.demo.po.UserRole;
import com.cloud.demo.service.api.IUserRoleService;
import com.cloud.demo.utils.StringUtil;
import com.cloud.demo.vo.UserRoleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @Author weiwei
 * @Date 2022/5/11 下午10:45
 * @Version 1.0
 * @Desc
 */
@Service
public class UserRoleService extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public List<UserRole> listAllRoles() {
        return this.list();
    }

    @Override
    public UserRoleVo getByCode(String roleCode) {
        UserRole role = this.getOne(new QueryWrapper<UserRole>().eq("role_code", roleCode));
        if (role == null) {
            return null;
        }
        return new UserRoleVo(role);
    }

    @Override
    public int insert(UserRoleVo userRoleVo) {
        return 0;
    }

    @Override
    public int update(UserRoleVo userRoleVo) {
        return 0;
    }

    @Override
    public int delete(Long id) {
        return 0;
    }

    @Override
    public List<String> getAllUserRoles(Long accountId) {
        if (Objects.isNull(accountId)) {
            return Collections.emptyList();
        }
        return userRoleMapper.getAllUserRoles(accountId);
    }
}
