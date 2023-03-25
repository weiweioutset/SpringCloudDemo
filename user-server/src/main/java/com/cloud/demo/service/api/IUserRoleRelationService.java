package com.cloud.demo.service.api;

import com.cloud.demo.po.UserRoleRelation;
import com.cloud.demo.vo.UserRoleRelationVo;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/5/25 下午11:01
 * @Version 1.0
 * @Desc
 */
public interface IUserRoleRelationService {
    /**
     * 新增关系
     */
    boolean insert(UserRoleRelationVo relation);

    /**
     * 删除关系
     */
    int delete(Long userId, Long roleId);

    /**
     * 获取用户所有角色
     */
    List<UserRoleRelationVo> listRelation(Long userId);
}
