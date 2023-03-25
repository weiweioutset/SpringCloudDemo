package com.cloud.demo.param;

import lombok.Data;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.demo.po.BasePo;

import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/5/15 下午3:55
 * @Version 1.0
 * @Desc
 */
@Data
public class BaseParam<T extends BasePo> {
    private Date createdTimeStart;
    private Date createdTimeEnd;

    public QueryWrapper<T> build() {
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge(null != this.createdTimeStart, "create_time", this.createdTimeStart)
                .le(null != this.createdTimeEnd, "create_time", this.createdTimeEnd);
        return queryWrapper;
    }
}

