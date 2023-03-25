package com.cloud.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.demo.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author weiwei
 * @Date 2022/4/22 下午10:48
 * @Version 1.0
 * @Desc
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
