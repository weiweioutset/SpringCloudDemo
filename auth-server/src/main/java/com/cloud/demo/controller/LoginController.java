package com.cloud.demo.controller;

import com.cloud.demo.utils.*;
import com.cloud.demo.vo.RefreshTokenRequest;
import com.cloud.demo.vo.Result;
import com.cloud.demo.vo.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Author weiwei
 * @Date 2022/5/8 上午11:35
 * @Version 1.0
 * @Desc 用户登录相关Controller
 */
@RestController
@RequestMapping("/user")
public class LoginController {
    @Autowired
    private LoginUtil loginUtil;

    /**
     * 重写login接口
     * @param userRequest
     * @return
     */
    @PostMapping("login")
    public Result<Map<String, Object>> postAccessToken(HttpServletRequest request,
                                                       @Validated UserRequest userRequest) {
        return loginUtil.login(userRequest, request);
    }

    /**
     * 重写refreshToken接口
     * @param tokenRequest
     * @return
     */
    @PostMapping("token")
    public Result<Map<String, Object>> refreshAccessToken(@Validated RefreshTokenRequest tokenRequest) {
        return loginUtil.login(tokenRequest);
    }

    /**
     * 重写login接口-测试用
     * @return
     */
    @GetMapping("loginTest")
    public Result<Map<String, Object>> getAccessToken(HttpServletRequest request,
                                                      @RequestParam("accountId") Long accountId,
                                                      @RequestParam(value = "clientId", defaultValue = "pc_client") String clientId,
                                                      @RequestParam("password") String password) throws Exception {
        UserRequest userRequest = new UserRequest();
        userRequest.setAccountId(accountId);
        userRequest.setClientId(clientId);
        // 测试密码加密
        String passwordEnc = RSAUtils.publicKeyEncrypt(password);
        userRequest.setPassword(URLEncoder.encode(passwordEnc, "UTF-8"));
        return loginUtil.login(userRequest, request);
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("logout")
    public Result<Boolean> logout(HttpServletRequest request) {
        return Result.success(loginUtil.logout(request));
    }
}
