package com.cloud.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.demo.po.MenuInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author weiwei
 * @Date 2022/7/17 下午3:51
 * @Version 1.0
 * @Desc
 */
@Repository
@Mapper
public interface MenuMapper extends BaseMapper<MenuInfo> {
}
