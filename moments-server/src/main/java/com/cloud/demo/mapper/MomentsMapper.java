package com.cloud.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.demo.po.Moments;
import com.cloud.demo.vo.MomentsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/7/30 下午3:53
 * @Version 1.0
 * @Desc
 */
@Repository
@Mapper
public interface MomentsMapper extends BaseMapper<Moments> {

}
