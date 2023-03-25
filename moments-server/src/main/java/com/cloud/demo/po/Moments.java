package com.cloud.demo.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.demo.form.MomentsForm;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/7/30 下午2:35
 * @Version 1.0
 * @Desc 动态实体类
 */
@Data
@NoArgsConstructor
@TableName("t_moments")
public class Moments {
    public Moments(MomentsForm form) {
        BeanUtils.copyProperties(form, this);
    }
    /**
     * 动态id
     */
    @TableField("id")
    private Long id;
    /**
     * 动态内容
     */
    @TableField("content")
    private String content;
    /**
     * 动态作者
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_time")
    private Date updateTime;
    /**
     * 动态类型 0图文
     */
    @TableField("type")
    private Integer type;
    /**
     * 是否有额外内容(如图片视频等)
     */
    @TableField("has_extra")
    private Boolean hasExtra;
    /**
     * 额外内容(保留字段)
     */
    @TableField("extra_data")
    private String extraData;
    /**
     * 可见范围:All公开/Private私密/Include部分可见/Except部分不可见
     * @see com.cloud.demo.enums.PublishScopeEnum
     */
    @TableField("publish_scope")
    private String publishScope;
    /**
     * 是否需要提醒谁看
     */
    @TableField("need_remind")
    private Boolean needRemind;
    /**
     * 是否被删除
     */
    @TableField("is_delete")
    private Boolean isDelete;
    /**
     * 发布IP地址
     */
    @TableField("publish_ip")
    private String publishIp;
    /**
     * 显示地址
     */
    @TableField("show_address")
    private String showAddress;
}
