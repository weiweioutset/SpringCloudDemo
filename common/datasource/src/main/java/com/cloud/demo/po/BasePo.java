package com.cloud.demo.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/5/12 下午11:24
 * @Version 1.0
 * @Desc
 */
@Data
public class BasePo implements Serializable {
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    @TableField(fill = FieldFill.INSERT, value = "create_by")
    private String createBy;

    @TableField(fill = FieldFill.INSERT, value = "create_time")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_by")
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_time")
    private Date updateTime;
}

