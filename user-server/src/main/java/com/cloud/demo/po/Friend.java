package com.cloud.demo.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.demo.constant.BaseConstant;
import com.cloud.demo.form.FriendAddForm;
import com.cloud.demo.form.FriendUpdateForm;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/7/21 下午9:53
 * @Version 1.0
 * @Desc 好友实体类
 */
@Data
@NoArgsConstructor
@TableName("t_friends")
public class Friend {
    public Friend(FriendAddForm addForm) {
        BeanUtils.copyProperties(addForm, this);
        this.setBlock(false);
        this.setFocus(false);
    }

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 好友id
     */
    @TableField("friend_id")
    private Long friendId;
    /**
     * 好友添加时间
     */
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    @JsonFormat(pattern = BaseConstant.PATTERN_A, timezone = BaseConstant.TIME_ZONE)
    private Date createTime;
    /**
     * 好友关系更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_time")
    @JsonFormat(pattern = BaseConstant.PATTERN_A, timezone = BaseConstant.TIME_ZONE)
    private Date updateTime;
    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
    /**
     * 是否仅聊天
     */
    @TableField("chats_only")
    private Boolean chatsOnly;
    /**
     * 不让他看我动态
     */
    @TableField("hide_my_posts")
    private Boolean hideMyPosts;
    /**
     * 不看他动态
     */
    @TableField("hide_his_posts")
    private Boolean hideHisPosts;
    /**
     * 添加来源
     */
    @TableField("from_source")
    private String fromSource;
    /**
     * 是否添加星标
     */
    @TableField("focus")
    private Boolean focus;
    /**
     * 是否加入黑名单
     */
    @TableField("block")
    private Boolean block;
    /**
     * 好友申请状态 0申请中 1通过 2拒绝
     * @see com.cloud.demo.enums.FriendApplyStatus
     */
    @TableField("apply_status")
    private Integer applyStatus;
}
