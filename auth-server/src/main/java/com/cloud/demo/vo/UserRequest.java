package com.cloud.demo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author weiwei
 * @Date 2022/7/3 下午4:07
 * @Version 1.0
 * @Desc
 */
@Data
@NoArgsConstructor
public class UserRequest implements Serializable {
    /**
     * 账号id
     */
    @NotNull(message = "账号不能为空")
    private Long accountId;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 客户端类型
     */
    @NotEmpty(message = "客户端类型不能为空")
    private String clientId;
    /**
     * 密码
     */
    @NotEmpty(message = "密码不能为空")
    private String password;
    /**
     * 短信验证码
     */
    private String smsCode;

}
