package com.cloud.demo.form;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.demo.param.BaseParam;

/**
 * @Author weiwei
 * @Date 2022/5/15 下午3:54
 * @Version 1.0
 * @Desc
 */
@Data
public class BaseQueryForm<P extends BaseParam> extends BaseForm {
    /**
     * 分页查询的参数，当前页数
     */
    private long current = 1;
    /**
     * 分页查询的参数，当前页面每页显示的数量
     */
    private long size = 10;

    /**
     * Form转化为Param
     *
     * @param clazz
     * @return
     */
    public P toParam(Class<P> clazz) {
        P p = BeanUtils.instantiateClass(clazz);
        BeanUtils.copyProperties(this, p);
        return p;
    }

    /**
     * 从form中获取page参数，用于分页查询参数
     *
     * @return
     */
    public Page getPage() {
        return new Page(this.getCurrent(), this.getSize());
    }

}