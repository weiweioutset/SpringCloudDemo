package com.cloud.demo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/6/30 下午9:59
 * @Version 1.0
 * @Desc 在线用户实体类
 */
@Data
@NoArgsConstructor
public class OnlineUserVo {
    public OnlineUserVo(UserLoginLogVo userLoginLogVo) {
        BeanUtils.copyProperties(userLoginLogVo, this);
    }
    /**
     * 账号id
     */
    private Long accountId;
    /**
     * token
     */
    private String token;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 登录时间
     */
    private Date loginTime;
    /**
     * 登录ip
     */
    private String loginIp;
    /**
     * 剩余时间
     */
    private Integer expiresIn;
    /**
     * 设备类型 Android/IOS/PC
     */
    private String clientType;
    /**
     * 设备系统 Windows10/MAC10.2/Android11 ETC.
     */
    private String clientOS;
}
