package com.cloud.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.demo.po.MomentExtraMedia;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Author weiwei
 * @Date 2022/7/30 下午3:53
 * @Version 1.0
 * @Desc
 */
@Repository
@Mapper
public interface MomentExtraMediaMapper extends BaseMapper<MomentExtraMedia> {
}
