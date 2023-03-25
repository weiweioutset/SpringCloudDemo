package com.cloud.demo.service.impl;

import com.cloud.demo.enums.ClientTypeEnum;
import com.cloud.demo.service.IUserClient;
import com.cloud.demo.service.IAccountManageService;
import com.cloud.demo.vo.OnlineUserVo;
import com.cloud.demo.vo.Page;
import com.cloud.demo.vo.Result;
import com.cloud.demo.vo.UserLoginLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author weiwei
 * @Date 2022/7/2 下午2:22
 * @Version 1.0
 * @Desc 用户管理服务，仅限系统管理员能够使用
 */
@Service
public class AccountManageService implements IAccountManageService {
    @Autowired
    private IUserClient userClient;
    @Autowired
    private RedisTokenStore redisTokenStore;

    /**
     * 获取所有在线用户(分页)
     * @param page
     * @return
     */
    @Override
    public List<OnlineUserVo> onlineUser(Page<OnlineUserVo> page) {
        // 获取所有在线的用户的Token
        List<OAuth2AccessToken> tokens = getAllToken();
        if (CollectionUtils.isEmpty(tokens)) {
            return page.getRecords();
        }

        // 处理分页信息
        long pageSize = page.getSize();
        long current = page.getCurrent();
        long jumpSize = pageSize * (current - 1);
        page.setTotal(tokens.size());

        List<OAuth2AccessToken> needTokens = tokens.stream()
                .sorted(Comparator.comparing(OAuth2AccessToken::getExpiresIn).reversed())
                .skip(jumpSize).limit(pageSize)
                .collect(Collectors.toList());

        List<OnlineUserVo> result = new ArrayList<>();

        // 获取所有key所对应的用户
        for (OAuth2AccessToken oAuth2AccessToken : needTokens) {
            // 获取accessToken对象中的additionalInformation属性
            Map<String, Object> userInfoMap = oAuth2AccessToken.getAdditionalInformation();
            if (userInfoMap.isEmpty()) {
                continue;
            }
            String accountId = userInfoMap.get("accountId").toString();
            String clientId = userInfoMap.getOrDefault("client_id", "").toString();
            String clientType = "";
            // 设置登录设备类型
            if (ClientTypeEnum.CLIENT_PC.getValue().equals(clientId)) {
                clientType = ClientTypeEnum.CLIENT_PC.getType();
            } else if (ClientTypeEnum.CLIENT_MOBILE.getValue().equals(clientId)) {
                clientType = ClientTypeEnum.CLIENT_MOBILE.getType();
            }
            // 获取用户登录日志
            Result<UserLoginLogVo> loginLog = userClient.getUserNewestLogin(Long.parseLong(accountId), clientType);
            if (!loginLog.isSuccess() || Objects.isNull(loginLog.getData())) {
                continue;
            }

            // 创建实体类
            OnlineUserVo onlineUserVo = new OnlineUserVo(loginLog.getData());
            // 设置剩余时间
            onlineUserVo.setExpiresIn(oAuth2AccessToken.getExpiresIn());
            result.add(onlineUserVo);
        }

        // 设置分页结果信息
        page.setRecords(result);
        return result;
    }

    /**
     * 获取在线用户数量
     * @return 在线用户总数
     */
    @Override
    public int countOnlineUser() {
        return getAllToken().size();
    }

    /**
     * 获取所有在线用户的Token信息
     * todo 请勿滥用
     * @return
     */
    private List<OAuth2AccessToken> getAllToken() {
        List<OAuth2AccessToken> existingAccessTokens = new ArrayList<>();
        existingAccessTokens.addAll(redisTokenStore.findTokensByClientId(ClientTypeEnum.CLIENT_MOBILE.getValue()));
        existingAccessTokens.addAll(redisTokenStore.findTokensByClientId(ClientTypeEnum.CLIENT_PC.getValue()));
        return existingAccessTokens;
    }

    /**
     * 将用户踢下线
     * todo 一定只能是系统管理员才能调用,注意权限控制
     * @param accountId 账号
     * @param clientType 客户端类型， All全部
     */
    @Override
    public int offlineUser(Long accountId, String clientType) {
        if (accountId == null || accountId <= 0) {
            return 0;
        }
        // 获取客户端枚举类
        ClientTypeEnum typeEnum = ClientTypeEnum.getEnum(clientType);
        List<OAuth2AccessToken> existingAccessTokens = new ArrayList<>();
        switch (typeEnum) {
            // 所有客户端,需要将所有客户端类型都取出来
            case CLIENT_ALL:
                existingAccessTokens.addAll(redisTokenStore.findTokensByClientIdAndUserName(ClientTypeEnum.CLIENT_MOBILE.getValue(), String.valueOf(accountId)));
                existingAccessTokens.addAll(redisTokenStore.findTokensByClientIdAndUserName(ClientTypeEnum.CLIENT_PC.getValue(), String.valueOf(accountId)));
                break;
            // PC端
            case CLIENT_PC:
                existingAccessTokens.addAll(redisTokenStore.findTokensByClientIdAndUserName(ClientTypeEnum.CLIENT_PC.getValue(), String.valueOf(accountId)));
                break;
            // 移动端
            case CLIENT_MOBILE:
                existingAccessTokens.addAll(redisTokenStore.findTokensByClientIdAndUserName(ClientTypeEnum.CLIENT_MOBILE.getValue(), String.valueOf(accountId)));
                break;
        }
        if (CollectionUtils.isEmpty(existingAccessTokens)) {
            return 0;
        }
        int onlineCount = 0;
        for (OAuth2AccessToken existingAccessToken : existingAccessTokens) {
            OAuth2RefreshToken refreshToken = null;
            if (existingAccessToken != null) {
                if (existingAccessToken.isExpired()) {
                    if (existingAccessToken.getRefreshToken() != null) {
                        refreshToken = existingAccessToken.getRefreshToken();
                        redisTokenStore.removeRefreshToken(refreshToken);
                    }
                } else {
                    onlineCount ++;
                    refreshToken = existingAccessToken.getRefreshToken();
                    redisTokenStore.removeRefreshToken(refreshToken);
                }
                redisTokenStore.removeAccessToken(existingAccessToken);
            }
        }
        return onlineCount;
    }
}