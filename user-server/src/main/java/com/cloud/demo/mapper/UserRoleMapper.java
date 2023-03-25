package com.cloud.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.demo.po.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/5/11 下午10:40
 * @Version 1.0
 * @Desc
 */
@Repository
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Select("SELECT role_code FROM role_info t\n" +
            "INNER JOIN user_role_relation t1 ON t.id = t1.role_id\n" +
            "WHERE t1.user_id = #{accountId} AND (t1.expire_time > NOW() OR t1.expire_time IS NULL)")
    List<String> getAllUserRoles(@Param("accountId") Long accountId);
}
