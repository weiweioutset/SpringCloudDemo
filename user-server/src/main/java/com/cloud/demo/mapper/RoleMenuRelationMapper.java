package com.cloud.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.demo.po.RoleMenuRelation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author weiwei
 * @Date 2022/7/17 下午4:13
 * @Version 1.0
 * @Desc
 */
@Repository
@Mapper
public interface RoleMenuRelationMapper extends BaseMapper<RoleMenuRelation> {

    /**
     * 删除菜单下的所有关系
     * @param menuId
     * @return
     */
    @Delete("DELETE FROM role_menu_relation WHERE menu_id = #{menuId}")
    int deleteByMenuId(@Param("menuId") Long menuId);

    /**
     * 删除角色对应的所有菜单关系
     * @param roleId
     * @return
     */
    @Delete("DELETE FROM role_menu_relation WHERE role_id = #{roleId}")
    int deleteByRoleId(@Param("roleId") Long roleId);

    @Insert("INSERT INTO role_menu_relation(role_id,menu_id,create_user,create_time)" +
            "VALUES (#{roleId}, #{menuId}, #{createUser}, #{createTime})" +
            "ON DUPLICATE KEY UPDATE create_user = #{createUser}, create_time = #{createTime}")
    int addRoleRelation(RoleMenuRelation relation);
}
