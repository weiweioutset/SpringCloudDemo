package com.cloud.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.demo.po.UserLoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author weiwei
 * @Date 2022/6/30 下午10:51
 * @Version 1.0
 * @Desc
 */
@Repository
@Mapper
public interface UserLoginLogMapper extends BaseMapper<UserLoginLog> {
    @Select("SELECT id,account_id,user_name,login_time,login_ip,client_type,client_os,login_type " +
            "FROM user_login_log " +
            "WHERE account_id = #{accountId} AND client_type = #{clientType} " +
            "ORDER BY id DESC LIMIT 1")
    UserLoginLog getNewest(@Param("accountId") Long accountId, @Param("clientType") String clientType);
}
