package com.cloud.demo.service;

import com.cloud.demo.vo.OnlineUserVo;
import com.cloud.demo.vo.Page;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/7/2 下午2:15
 * @Version 1.0
 * @Desc
 */
public interface IAccountManageService {
    /**
     * 获取所有在线用户
     * @return
     */
    List<OnlineUserVo> onlineUser(Page<OnlineUserVo> page);

    /**
     * 获取当前在线用户的数量(最大只支持2万)
     */
    int countOnlineUser();

    /**
     * 将用户踢下线
     * @param accountId 账号
     * @param clientType 客户端类型， All全部
     */
    int offlineUser(Long accountId, String clientType);
}
