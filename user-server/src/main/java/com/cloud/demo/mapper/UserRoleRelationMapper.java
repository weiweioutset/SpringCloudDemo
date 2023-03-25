package com.cloud.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.demo.po.UserRoleRelation;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @Author weiwei
 * @Date 2022/5/25 下午11:05
 * @Version 1.0
 * @Desc
 */
@Repository
@Mapper
public interface UserRoleRelationMapper extends BaseMapper<UserRoleRelation> {
}
