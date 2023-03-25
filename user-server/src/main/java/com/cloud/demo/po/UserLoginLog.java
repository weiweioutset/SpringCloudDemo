package com.cloud.demo.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.demo.constant.BaseConstant;
import com.cloud.demo.vo.UserLoginLogVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/6/30 下午10:47
 * @Version 1.0
 * @Desc
 */
@Data
@NoArgsConstructor
@TableName("user_login_log")
public class UserLoginLog {
    public UserLoginLog(UserLoginLogVo loginLogVo) {
        BeanUtils.copyProperties(loginLogVo, this);
    }
    /**
     * 主键
     */
    @TableField(exist = false)
    private Long id;
    /**
     * 账号id
     */
    @TableField("account_id")
    private Long accountId;
    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;
    /**
     * 登录时间
     */
    @TableField(fill = FieldFill.INSERT, value = "login_time")
    @JsonFormat(pattern = BaseConstant.PATTERN_A, timezone = BaseConstant.TIME_ZONE)
    private Date loginTime;
    /**
     * 登录ip
     */
    @TableField("login_ip")
    private String loginIp;
    /**
     * 设备类型 Android/IOS/PC
     */
    @TableField("client_type")
    private String clientType;
    /**
     * 设备系统 Windows10/MAC10.2/Android11 ETC.
     */
    @TableField("client_os")
    private String clientOS;
    /**
     * 登录方式 0密码登录 1扫码登录
     */
    @TableField("login_type")
    private Integer loginType;
}
