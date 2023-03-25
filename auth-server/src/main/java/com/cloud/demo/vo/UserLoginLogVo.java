package com.cloud.demo.vo;

import com.cloud.demo.constant.BaseConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/7/2 下午2:14
 * @Version 1.0
 * @Desc
 */
@Data
@NoArgsConstructor
public class UserLoginLogVo {
    /**
     * 账号id
     */
    private Long accountId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 登录时间
     */
    @JsonFormat(pattern = BaseConstant.PATTERN_A, timezone = "GMT+8")
    private Date loginTime;
    /**
     * 登录ip
     */
    private String loginIp;
    /**
     * 设备类型 Android/IOS/PC
     */
    private String clientType;
    /**
     * 设备系统 Windows10/MAC10.2/Android11 ETC.
     */
    private String clientOS;
    /**
     * 登录方式 0密码登录 1扫码登录
     */
    private Integer loginType;
}
