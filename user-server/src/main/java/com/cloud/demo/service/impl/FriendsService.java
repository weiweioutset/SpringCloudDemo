package com.cloud.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.enums.FriendApplyStatus;
import com.cloud.demo.enums.FromSourceEnums;
import com.cloud.demo.exception.CommonException;
import com.cloud.demo.form.FriendAddForm;
import com.cloud.demo.form.FriendUpdateForm;
import com.cloud.demo.mapper.FriendMapper;
import com.cloud.demo.mq.publisher.BasicPublisher;
import com.cloud.demo.po.Friend;
import com.cloud.demo.service.api.IFriendsService;
import com.cloud.demo.utils.LoginInfoUtil;
import com.cloud.demo.utils.StringUtil;
import com.cloud.demo.vo.FriendVo;
import com.cloud.demo.vo.UserVo;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author weiwei
 * @Date 2022/7/21 下午10:02
 * @Version 1.0
 * @Desc 好友Service
 */
@Service
public class FriendsService extends ServiceImpl<FriendMapper, Friend> implements IFriendsService {
    private final Logger LOGGER = LoggerFactory.getLogger(FriendsService.class);
    @Autowired
    private LoginInfoUtil loginInfoUtil;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private BasicPublisher publisher;

    @Override
    public List<FriendVo> listMyFriends() {
        // 获取登录用户
        UserVo loginUser = loginInfoUtil.userInfo();
        Long userId = loginUser.getAccountId();
        return friendMapper.listFriends(userId, 0);
    }

    @Override
    public List<FriendVo> listChatsOnlyFriends() {
        // 获取登录用户
        UserVo loginUser = loginInfoUtil.userInfo();
        Long userId = loginUser.getAccountId();
        return friendMapper.listChatsOnlyFriends(userId);
    }

    @Override
    public List<FriendVo> listBlockFriends() {
        // 获取登录用户
        UserVo loginUser = loginInfoUtil.userInfo();
        Long userId = loginUser.getAccountId();
        return friendMapper.listFriends(userId, 1);
    }

    @Override
    public FriendVo friendInfo(Long friendId) {
        // 获取登录用户
        UserVo loginUser = loginInfoUtil.userInfo();
        Long userId = loginUser.getAccountId();
        Friend friend = this.getOne(new QueryWrapper<Friend>()
                .eq("user_id", userId)
                .eq("friend_id", friendId)
                .eq("apply_status", FriendApplyStatus.AGREE_APPLY.getStatus()));
        if (!Objects.isNull(friend)) {
            FriendVo friendVo = new FriendVo(friend);
            // 获取好友详细信息
            UserVo friendInfo = userDetailService.getByAccountId(friendId);
            if (Objects.isNull(friendInfo)) {
                LOGGER.info("好友账号{}不存在", friendId);
                throw new CommonException(CommonExceptionEnum.ACCOUNT_NOT_EXIST);
            }
            friendVo.setFriendInfo(friendInfo);
            return friendVo;
        } else {
            throw new CommonException(CommonExceptionEnum.NON_FRIEND_RELATIONSHIP);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addFriend(FriendAddForm form) {
        // 检测来源
        form.setFromSource(checkFromSource(form.getFromSource()));
        // 获取登录用户
        UserVo loginUser = loginInfoUtil.userInfo();
        // 检测是否已经是好友
        Friend friendApply = this.getOne(new QueryWrapper<Friend>()
                .eq("user_id", form.getFriendId())
                .eq("friend_id", loginUser.getAccountId()));
        if (Objects.isNull(friendApply)) {
            Friend friend = new Friend(form);
            friend.setUserId(loginUser.getAccountId());
            Date current = new Date();
            friend.setCreateTime(current);
            friend.setUpdateTime(current);
            boolean result = this.save(friend);
            if (result) {
                // 发送好友申请
                publisher.sendFriendApplyMsg(loginUser.getAccountId(), form.getFriendId(), form.getApplyMessage());
            }
            return result;
        } else if (FriendApplyStatus.AGREE_APPLY.getStatus().equals(friendApply.getApplyStatus())) {
            LOGGER.info("已经是好友关系了，无需重复申请,from[{}] to [{}]", form.getFriendId(), loginUser.getAccountId());
            throw new CommonException(CommonExceptionEnum.ALREADY_FRIEND_RELATIONSHIP);
        } else if (FriendApplyStatus.REJECT_APPLY.getStatus().equals(friendApply.getApplyStatus())) {
            friendApply.setApplyStatus(FriendApplyStatus.APPLY_ING.getStatus());
            int result = friendMapper.updateApplyStatus(friendApply);
            return result > 0;
        } else {
            LOGGER.info("异常状态");
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean dealFriendApply(FriendAddForm form) {
        // 获取登录用户
        UserVo loginUser = loginInfoUtil.userInfo();
        // 检测是否有好友申请存在
        Friend friendApply = this.getOne(new QueryWrapper<Friend>()
                .eq("user_id", form.getFriendId())
                .eq("friend_id", loginUser.getAccountId()));
        if (Objects.isNull(friendApply)) {
            throw new CommonException(CommonExceptionEnum.NO_FRIEND_APPLY);
        }
        // 申请状态判断
        if (FriendApplyStatus.AGREE_APPLY.getStatus().equals(friendApply.getApplyStatus())) {
            LOGGER.info("已经是好友关系了，无需重复处理,from[{}] to [{}]", form.getFriendId(), loginUser.getAccountId());
            throw new CommonException(CommonExceptionEnum.ALREADY_FRIEND_RELATIONSHIP);
        } else if (FriendApplyStatus.REJECT_APPLY.getStatus().equals(friendApply.getApplyStatus())) {
            LOGGER.info("拒绝了申请，请重新申请,from[{}] to [{}]", form.getFriendId(), loginUser.getAccountId());
            throw new CommonException(CommonExceptionEnum.ADD_FRIEND_FAIL);
        }
        boolean result;
        if (form.getAgreeApply()) {
            form.setFromSource(friendApply.getFromSource());
            form.setFromSource(checkFromSource(form.getFromSource()));
            Friend friend = new Friend(form);
            friend.setUserId(loginUser.getAccountId());
            Date current = new Date();
            friend.setCreateTime(current);
            friend.setUpdateTime(current);
            friend.setApplyStatus(FriendApplyStatus.AGREE_APPLY.getStatus());
            result = this.save(friend);
            if (result) {
                friendApply.setApplyStatus(FriendApplyStatus.AGREE_APPLY.getStatus());
                friendMapper.updateApplyStatus(friendApply);
                // 发送打招呼消息
                publisher.addFriendSuccessMsg(loginUser.getAccountId(), friend.getFriendId());
            }
        } else {
            friendApply.setApplyStatus(FriendApplyStatus.REJECT_APPLY.getStatus());
            int updateCount = friendMapper.updateApplyStatus(friendApply);
            result = updateCount > 0;
            // 发送拒绝好友申请消息
            publisher.rejectFriendApplyMsg(loginUser.getAccountId(), form.getFriendId(), form.getApplyMessage());
        }
        return result;
    }

    @Override
    public boolean deleteFriend(Long friendId) {
        // 获取登录用户
        UserVo loginUser = loginInfoUtil.userInfo();
        Long userId = loginUser.getAccountId();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("friend_id", friendId);
        boolean result = this.removeByMap(map);
        if (result) {
            // 清除聊天记录(MQ异步)
            // 清除对方动态时间轴中关于自己的所有动态(MQ异步)
            // 清除自己的动态时间轴中所有关于对方的动态(MQ异步)
            publisher.deleteFriendMsg(userId, friendId);
        }
        return result;
    }

    @Override
    public boolean updateFriendInfo(Map<String, Object> params) {
        if (params.isEmpty() || !params.containsKey("friendId") || params.size() < 2) {
            throw new CommonException(CommonExceptionEnum.INVALID_PARAMS);
        }
        // 获取登录用户
        UserVo loginUser = loginInfoUtil.userInfo();
        Long userId = loginUser.getAccountId();
        Long friendId = (long) (Integer) params.get("friendId");
        UpdateWrapper<Friend> updateWrapper = new UpdateWrapper<>();
        boolean canUpdate = false;
        // 判断可以更改的设置
        // 备注
        if (params.containsKey("remark")) {
            updateWrapper.set("remark", (String) params.get("remark"));
            canUpdate = true;
        }
        // 是否仅聊天
        if (params.containsKey("chatsOnly")) {
            boolean chatsOnly = (Boolean) params.get("chatsOnly");
            updateWrapper.set("chats_only", chatsOnly);
            canUpdate = true;
            if (chatsOnly) {
                updateWrapper.set("hide_my_posts", true);
                updateWrapper.set("hide_his_posts", true);
            } else {
                updateWrapper.set("hide_my_posts", false);
                updateWrapper.set("hide_his_posts", false);
            }
        }
        // 不让他看我动态
        if (params.containsKey("hideMyPosts")) {
            boolean hideMyPosts = (Boolean)params.get("hideMyPosts");
            updateWrapper.set("hide_my_posts", hideMyPosts);
            canUpdate = true;
        }
        // 不看他动态
        if (params.containsKey("hideHisPosts")) {
            boolean hideHisPosts = (Boolean)params.get("hideHisPosts");
            updateWrapper.set("hide_his_posts", hideHisPosts);
            canUpdate = true;
        }
        // 是否关注
        if (params.containsKey("focus")) {
            updateWrapper.set("focus", (Boolean)params.get("focus"));
            canUpdate = true;
        }
        // 是否加入黑名单
        if (params.containsKey("block")) {
            boolean block = (Boolean) params.get("block");
            updateWrapper.set("block", block);
            canUpdate = true;
            if (block) {
                updateWrapper.set("hide_my_posts", true);
                updateWrapper.set("hide_his_posts", true);
            } else {
                updateWrapper.set("hide_my_posts", false);
                updateWrapper.set("hide_his_posts", false);
            }
        }
        if (!canUpdate) {
            LOGGER.info("更新好友信息失败，参数错误");
            throw new CommonException(CommonExceptionEnum.INVALID_PARAMS);
        }
        updateWrapper.eq("user_id", loginUser.getAccountId());
        updateWrapper.eq("friend_id", friendId);
        boolean result = this.update(null, updateWrapper);

        // 发送异步消息
        // 是否仅聊天/是否加入黑名单
        if (params.containsKey("chatsOnly") || params.containsKey("block")) {
            boolean remove = false;
            if (params.containsKey("block")) {
                remove = (Boolean) params.get("block");
            }
            if (!remove && params.containsKey("chatsOnly")) {
                remove = (Boolean) params.get("chatsOnly");
            }
            if (remove) {
                // 清除对方动态时间轴中关于自己的所有动态(MQ异步)
                publisher.removeMyMoments(userId, friendId);
                // 清除自己的动态时间轴中所有关于对方的动态(MQ异步)
                publisher.removeFriendMoments(userId, friendId);
            } else {
                // 拉取对方的动态到自己的时间轴(MQ异步)
                publisher.pullFriendMomentMsg(userId, friendId);
                // 推送自己的动态到对方的(MQ异步)
                publisher.pushMyMomentMsg(userId, friendId);
            }
        }
        // 不让他看我动态
        if (params.containsKey("hideMyPosts")) {
            boolean hideMyPosts = (Boolean)params.get("hideMyPosts");
            if (hideMyPosts) {
                // 清除对方动态时间轴中关于自己的所有动态(MQ异步)
                publisher.removeMyMoments(userId, friendId);
            } else {
                // 推送自己的动态到对方的(MQ异步)
                publisher.pushMyMomentMsg(userId, friendId);
            }
        }
        // 不看他动态
        if (params.containsKey("hideHisPosts")) {
            boolean hideHisPosts = (Boolean)params.get("hideHisPosts");
            if (hideHisPosts) {
                // 清除自己的动态时间轴中所有关于对方的动态(MQ异步)
                publisher.removeFriendMoments(userId, friendId);
            } else {
                // 拉取对方的动态到自己的时间轴(MQ异步)
                publisher.pullFriendMomentMsg(userId, friendId);
            }
        }

        return result;
    }

    /**
     * 获取所有有权可见自己动态的好友列表
     * 去除黑名单/仅聊天/不让他看我/或者对方设置了相关权限的
     * @param userId
     * @return
     */
    @Override
    public List<Long> listShouldShowMomentFriends(long userId) {
        if (userId <= 0) {
            return null;
        }
        List<Long> list = friendMapper.listShouldShowMomentFriends(userId);
        return list.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public Boolean canVisitMoments(long userId, long friendId) {
        Integer canVisitMoments = friendMapper.canVisitMoments(userId, friendId);
        return canVisitMoments != null && canVisitMoments > 0;
    }

    /**
     * 检测添加来源是否合法
     * @param fromSource
     * @return
     */
    private String checkFromSource(String fromSource) {
        if (StringUtil.isEmpty(fromSource)) {
            LOGGER.info("添加好友失败，来源未知");
            return FromSourceEnums.UNKNOWN_SOURCE.getCode();
        }
        if (EnumUtils.isValidEnum(FromSourceEnums.class, fromSource)) {
            return fromSource;
        }
        return FromSourceEnums.UNKNOWN_SOURCE.getCode();
    }

}
