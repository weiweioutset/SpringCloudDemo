package com.cloud.demo.utils;

import com.cloud.demo.constant.BaseConstant;
import com.cloud.demo.constant.RedisKeyConstant;
import com.cloud.demo.enums.ClientTypeEnum;
import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.exception.CommonException;
import com.cloud.demo.service.IUserClient;
import com.cloud.demo.vo.*;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author weiwei
 * @Date 2022/7/3 下午4:12
 * @Version 1.0
 * @Desc 登录工具类
 * 重写登录流程，关键调用：TokenEndpoint.postAccessToken
 */
@Component
public class LoginUtil {
    private Logger logger = LoggerFactory.getLogger(LoginUtil.class);
    @Value("${client.secret}")
    private String clientSecret;
    @Autowired
    private TokenEndpoint tokenEndpoint;
    @Autowired
    private RedisTokenStore redisTokenStore;
    @Autowired
    private IUserClient userClient;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 账号登录
     * @param userRequest
     * @param request
     * @return
     */
    public Result<Map<String, Object>> login(UserRequest userRequest, HttpServletRequest request) {
        Map<String, Object> result;
        String accountId = userRequest.getAccountId() + "";
        try {
            result = executeLogin(userRequest, request);
        } catch (Exception cause) {
            if (cause instanceof UsernameNotFoundException) {
                logger.info("用户未注册【{}】", accountId);
                return Result.fail(CommonExceptionEnum.ACCOUNT_NOT_EXIST);
            } else if (cause instanceof CommonException) {
                logger.info("登录异常,error:{}", ((CommonException) cause).getException().getMessage());
                return Result.fail(((CommonException) cause).getException());
            } else if(cause instanceof InvalidGrantException) {
                logger.info("账号或密码错误【{}】", accountId);
                return Result.fail(CommonExceptionEnum.PASSWORD_ERROR);
            } else if (cause instanceof NoSuchClientException){
                logger.info("客户端类型错误【{}】", accountId);
                return Result.fail(CommonExceptionEnum.NO_SUCH_CLIENT);
            } else {
                Throwable childCause = cause.getCause();
                if (childCause instanceof CommonException) {
                    logger.info("登录异常,error:{}", ((CommonException) childCause).getException().getMessage());
                    return Result.fail((CommonException) childCause);
                }
                logger.error("未知错误", cause);
                return Result.fail(CommonExceptionEnum.UNKNOWN_ERROR);
            }
        }
        return Result.success(result);
    }

    /**
     * 账号登录
     * 记录登录日志
     * @param userRequest
     * @return
     */
    public Map<String, Object> executeLogin(UserRequest userRequest, HttpServletRequest request) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken accessToken = token(userRequest);
        // 登录成功，增加登陆日志，只在登录的时候记录，刷新令牌时不需要
        addLoginLog(request, userRequest, accessToken.getAdditionalInformation());
        // 将用户信息放入Redis
        saveUserInfo(userRequest.getAccountId() + "", accessToken.getValue());
        // todo 统计同时在线用户
        return getTokenInfo(accessToken);
    }

    /**
     * 从token中获取信息
     * @param accessToken
     * @return
     */
    private Map<String, Object> getTokenInfo(OAuth2AccessToken accessToken) {
        Map<String, Object> resultMap = Maps.newLinkedHashMap();
        // token信息
        resultMap.put("accessToken", accessToken.getValue());
        resultMap.put("tokenType", accessToken.getTokenType());
        resultMap.put("expiresIn", accessToken.getExpiresIn());
        resultMap.putAll(accessToken.getAdditionalInformation());
        // refreshToken
        String refreshToken = accessToken.getRefreshToken() == null ? null : accessToken.getRefreshToken().getValue();
        resultMap.put("refreshToken", refreshToken);
        // 权限信息
        Collection<? extends GrantedAuthority> authorities =
                redisTokenStore.readAuthentication(accessToken).getUserAuthentication().getAuthorities();
        List<String> list = new ArrayList<>();
        for (GrantedAuthority authority : authorities) {
            list.add(authority.getAuthority());
        }
        resultMap.put("authorities", list);
        return resultMap;
    }

    /**
     * 账号登录
     * @param tokenRequest
     * @return
     */
    public Result<Map<String, Object>> login(RefreshTokenRequest tokenRequest) {
        Map<String, Object> result;
        try {
            result = refreshAccessToken(tokenRequest);
        } catch (Exception cause) {
            if (cause instanceof CommonException) {
                return Result.fail((CommonException) cause);
            }
            Throwable childCause = cause.getCause();
            if (childCause instanceof CommonException) {
                logger.info("登录异常,error:{}", ((CommonException) childCause).getException().getMessage());
                return Result.fail((CommonException) childCause);
            }
            logger.error("未知错误", cause);
            return Result.fail(CommonExceptionEnum.UNKNOWN_ERROR);
        }
        return Result.success(result);
    }

    /**
     * 刷新令牌
     * @param tokenRequest
     * @return
     */
    public Map<String, Object> refreshAccessToken(RefreshTokenRequest tokenRequest) throws HttpRequestMethodNotSupportedException {
        OAuth2AccessToken accessToken = token(tokenRequest);
        String accountId = accessToken.getAdditionalInformation().get("accountId").toString();
        // 将用户信息放入Redis
        saveUserInfo(accountId, accessToken.getValue());
        // todo 统计同时在线用户
        return getTokenInfo(accessToken);
    }

    /**
     * 保存用户登录信息
     * @param accountId
     * @param token
     */
    private void saveUserInfo(String accountId, String token) {
        if (StringUtil.isEmpty(accountId) || StringUtil.isEmpty(token)) {
            return;
        }
        UserVo userInfo = userClient.getUserDetail(accountId);
        if (userInfo == null) {
            logger.info("用户数据为空{}", accountId);
        }
        redisUtils.setValue(token, userInfo, (long) RedisKeyConstant.TOKEN_EXPIRE_TIME_SECOND, TimeUnit.SECONDS);
    }

    /**
     * 记录登录日志
     * @param userRequest
     * @param userInfo
     */
    public void addLoginLog(HttpServletRequest request, UserRequest userRequest, Map<String, Object> userInfo) {
        logger.info("账号[{}]登录成功,记录登录日志", userRequest.getAccountId());
        UserLoginLogVo loginLogVo = new UserLoginLogVo();
        String clientId = userRequest.getClientId();
        // 设置登录设备类型
        ClientTypeEnum typeEnum = ClientTypeEnum.getEnum(clientId);
        loginLogVo.setClientType(typeEnum.getType());
        // 设置登录方式
        loginLogVo.setLoginType(1);
        // 设置登录时间
        loginLogVo.setLoginTime(new Date());
        // 设置登录系统
        loginLogVo.setClientOS(DeviceUtil.getSystemOS(request));
        // 设置登录Ip
        loginLogVo.setLoginIp(IPUtils.getIPAddress(request));
        loginLogVo.setAccountId(userRequest.getAccountId());
        loginLogVo.setUserName(userInfo.getOrDefault("userFullName", "未知用户").toString());
        // 保存登录日志
        userClient.loginLog(loginLogVo);
    }

    /**
     * 获取登录凭证(密码方式)
     * @param userRequest
     * @return
     */
    public OAuth2AccessToken token(UserRequest userRequest) throws HttpRequestMethodNotSupportedException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_secret", clientSecret);
        parameters.put("client_id", userRequest.getClientId());
        parameters.put("grant_type", "password");
        parameters.put("username", userRequest.getAccountId() + "");
        parameters.put("password", userRequest.getPassword());
        parameters.put("scope", "all");
        User user = new User(userRequest.getClientId(), clientSecret,new ArrayList<>());
        Principal principal = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        return tokenEndpoint.postAccessToken(principal, parameters).getBody();
    }

    /**
     * 获取登录凭证(refreshToken方式)
     * @param tokenRequest
     * @return
     */
    public OAuth2AccessToken token(RefreshTokenRequest tokenRequest) throws HttpRequestMethodNotSupportedException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("client_secret", clientSecret);
        parameters.put("client_id", tokenRequest.getClientId());
        parameters.put("grant_type", "refresh_token");
        parameters.put("refresh_token", tokenRequest.getRefreshToken());
        parameters.put("scope", "all");
        User user = new User(tokenRequest.getClientId(), clientSecret,new ArrayList<>());
        Principal principal = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        return tokenEndpoint.postAccessToken(principal, parameters).getBody();
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    public boolean logout(HttpServletRequest request) {
        if (request == null || StringUtil.isEmpty(request.getHeader("Authorization"))) {
            logger.info("退出登录失败，凭证为空");
            return false;
        }
        String token = request.getHeader("Authorization").replace("Bearer","").trim();
        OAuth2AccessToken accessToken = redisTokenStore.readAccessToken(token);
        if (accessToken == null) {
            logger.info("退出登录失败，凭证错误");
            // token错误，退出登录失败
            return false;
        }
        if (accessToken.isExpired()) {
            logger.info("退出登录失败，账号已经离线");
            // 如果账号凭证已经过期，需要移除已过期的凭证，返回失败
            redisTokenStore.removeAccessToken(accessToken);
            return false;
        } else {
            // 如果账号凭证有效，还需要删除refreshToken
            redisTokenStore.removeRefreshToken(accessToken.getRefreshToken());
        }
        logger.info("退出登录成功,[{}]", accessToken.getAdditionalInformation().get("accountId"));
        redisTokenStore.removeAccessToken(accessToken);
        return true;
    }
}
