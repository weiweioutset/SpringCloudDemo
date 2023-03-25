package com.cloud.demo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * @Author weiwei
 * @Date 2022/7/13 下午10:57
 * @Version 1.0
 * @Desc
 */
@Data
@NoArgsConstructor
public class RefreshTokenRequest implements Serializable {
    /**
     * refreshToken
     */
    @NotEmpty(message = "令牌不能为空")
    private String refreshToken;
    /**
     * refreshToken
     */
    @NotEmpty(message = "令牌不能为空")
    private String accessToken;
    /**
     * 客户端类型
     */
    @NotEmpty(message = "客户端类型不能为空")
    private String clientId;
}
