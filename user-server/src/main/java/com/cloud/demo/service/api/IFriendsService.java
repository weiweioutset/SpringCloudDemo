package com.cloud.demo.service.api;

import com.cloud.demo.form.FriendAddForm;
import com.cloud.demo.vo.FriendVo;

import java.util.List;
import java.util.Map;

/**
 * @Author weiwei
 * @Date 2022/7/21 下午10:02
 * @Version 1.0
 * @Desc
 */
public interface IFriendsService {
    /**
     * 获取我的所有好友(黑名单除外)
     * @return
     */
    List<FriendVo> listMyFriends();

    /**
     * 获取“仅聊天”的好友列表
     * @return
     */
    List<FriendVo> listChatsOnlyFriends();

    /**
     * 获取“黑名单”的好友列表
     * @return
     */
    List<FriendVo> listBlockFriends();

    /**
     * 获取朋友详细信息
     * @param friendId
     * @return
     */
    FriendVo friendInfo(Long friendId);

    /**
     * 添加好友（只是发送好友申请）
     * @param form
     * @return
     */
    boolean addFriend(FriendAddForm form);

    /**
     * 处理好友申请
     * @param form
     * @return
     */
    boolean dealFriendApply(FriendAddForm form);

    /**
     * 删除好友
     * @param friendId
     * @return
     */
    boolean deleteFriend(Long friendId);

    /**
     * 更新朋友关系
     * @param params
     * @return
     */
    boolean updateFriendInfo(Map<String, Object> params);

    /**
     * 获取所有有权可见自己动态的好友列表
     * @param userId
     * @return
     */
    List<Long> listShouldShowMomentFriends(long userId);

    /**
     * 是否有权访问对方动态
     * @param userId
     * @param friendId
     * @return
     */
    Boolean canVisitMoments(long userId, long friendId);
}
