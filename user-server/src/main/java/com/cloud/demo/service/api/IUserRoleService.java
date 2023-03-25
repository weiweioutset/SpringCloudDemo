package com.cloud.demo.service.api;

import com.cloud.demo.po.UserRole;
import com.cloud.demo.vo.UserRoleVo;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/5/11 下午10:42
 * @Version 1.0
 * @Desc
 */
public interface IUserRoleService {
    /**
     * 获取所有角色
     * @return
     */
    List<UserRole> listAllRoles();
    /**
     * 根据角色代码获取角色信息
     * @param roleCode
     * @return
     */
    UserRoleVo getByCode(String roleCode);
    /**
     * 新增角色
     * @param userRoleVo
     * @return
     */
    int insert(UserRoleVo userRoleVo);

    /**
     * 修改角色
     * @param userRoleVo
     * @return
     */
    int update(UserRoleVo userRoleVo);

    /**
     * 删除角色
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * 获取用户所有有效的角色
     * @param accountId
     * @return
     */
    List<String> getAllUserRoles(Long accountId);
}
