package com.cloud.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.demo.mapper.UserRoleRelationMapper;
import com.cloud.demo.po.UserRoleRelation;
import com.cloud.demo.service.api.IUserRoleRelationService;
import com.cloud.demo.vo.UserRoleRelationVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/5/25 下午11:04
 * @Version 1.0
 * @Desc
 */
@Service
public class UserRoleRelationService extends ServiceImpl<UserRoleRelationMapper, UserRoleRelation> implements IUserRoleRelationService {
    @Override
    public boolean insert(UserRoleRelationVo relation) {
        Assert.notNull(relation, "参数错误");
        Assert.notNull(relation.getRoleId(), "角色Id不能为空");
        Assert.notNull(relation.getUserId(), "用户Id不能为空");
        return this.save(new UserRoleRelation(relation));
    }

    @Override
    public int delete(Long userId, Long roleId) {
        return 0;
    }

    @Override
    public List<UserRoleRelationVo> listRelation(Long userId) {
        return null;
    }
}
