package com.cloud.demo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/7/30 下午3:18
 * @Version 1.0
 * @Desc 动态额外媒体信息实体类
 */
@Data
@NoArgsConstructor
@TableName("moment_extra_media")
public class MomentExtraMedia {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 动态id
     */
    @TableField("moment_id")
    private Long momentId;
    /**
     * 图片/视频地址
     */
    @TableField("media_path")
    private String mediaPath;
    /**
     * 图片缩略地址
     */
    @TableField("thumbnail")
    private String thumbnail;
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 排序
     */
    @TableField("sort_index")
    private Integer sortIndex;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    private Date createTime;
}
