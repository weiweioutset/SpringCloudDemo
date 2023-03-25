package com.cloud.demo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.cloud.demo.vo.UserVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/4/22 下午10:30
 * @Version 1.0
 * @Desc
 */
@Data
@NoArgsConstructor
@TableName("user_info")
public class User {
    public User(UserVo userVo) {
        BeanUtils.copyProperties(userVo, this);
    }
    /**
     * 账号
     */
    @TableField("account_id")
    private Long accountId;
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 英文名
     */
    @TableField("english_name")
    private String englishName;
    /**
     * 性别
     */
    @TableField
    private Integer gender;
    /**
     * 注册时间
     */
    @TableField(fill = FieldFill.INSERT, value = "register_time")
    private Date registerTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_time")
    private Date updateTime;
    /**
     * 电话
     */
    @TableField
    private String phone;
    /**
     * 邮箱
     */
    @TableField
    private String email;
    /**
     * 密码（加密）
     */
    @TableField
    private String password;
    /**
     * 生日
     */
    @TableField
    private Date birthday;
    /**
     * 账号状态 0 申请中 1有效 2删除 3封号
     */
    @TableField
    private Integer status;
    /**
     * 头像地址
     */
    @TableField
    private String avatar;
    /**
     * 头像缩略图地址
     */
    @TableField("thumbnail_avatar")
    private String thumbnailAvatar;
}
