package com.cloud.demo.controller;

import com.cloud.demo.form.FriendAddForm;
import com.cloud.demo.service.api.IFriendsService;
import com.cloud.demo.vo.FriendVo;
import com.cloud.demo.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author weiwei
 * @Date 2022/7/24 下午12:35
 * @Version 1.0
 * @Desc
 */
@RestController
@RequestMapping("friend")
public class FriendController {
    @Autowired
    private IFriendsService friendsService;

    /**
     * 发送好友申请
     * @param addForm
     * @return
     */
    @PostMapping("apply")
    public Result<Boolean> applyFriend(@RequestBody @Validated FriendAddForm addForm) {
        return Result.success(friendsService.addFriend(addForm));
    }

    /**
     * 处理好友申请
     * @param addForm
     * @return
     */
    @PostMapping("deal")
    public Result<Boolean> dealFriendApply(@RequestBody @Validated FriendAddForm addForm) {
        return Result.success(friendsService.dealFriendApply(addForm));
    }

    /**
     * 获取好友详情
     * @param friendId
     * @return
     */
    @GetMapping("info")
    public Result<FriendVo> info(@RequestParam("friendId") Long friendId) {
        return Result.success(friendsService.friendInfo(friendId));
    }

    /**
     * 获取我的好友列表
     * @return
     */
    @GetMapping("list")
    public Result<List<FriendVo>> list() {
        return Result.success(friendsService.listMyFriends());
    }

    /**
     * 列出黑名单
     * @return
     */
    @GetMapping("block")
    public Result<List<FriendVo>> blockFriendList() {
        return Result.success(friendsService.listBlockFriends());
    }

    /**
     * 更新好友信息
     * 加入黑名单、修改备注等
     * @param params
     * @return
     */
    @PutMapping("info")
    public Result<Boolean> updateFriendInfo(@RequestBody Map<String, Object> params) {
        return Result.success(friendsService.updateFriendInfo(params));
    }

    /**
     * 删除好友关系
     * @param friendId
     * @return
     */
    @DeleteMapping("delete")
    public Result<Boolean> deleteFriend(@RequestParam("friendId") Long friendId) {
        return Result.success(friendsService.deleteFriend(friendId));
    }

    /**
     * 获取所有有权可见自己动态的好友列表
     * @param userId
     * @return
     */
    @GetMapping("shouldMomentFriends")
    public Result<List<Long>> listShouldShowMomentFriends(@RequestParam("userId") long userId) {
        return Result.success(friendsService.listShouldShowMomentFriends(userId));
    }

    /**
     * 是否有权访问对方动态
     * @param userId
     * @param friendId
     * @return
     */
    @GetMapping("canVisitMoments")
    public Result<Boolean> canVisitMoments(@RequestParam("userId") long userId,
                                           @RequestParam("friendId") long friendId) {
        return Result.success(friendsService.canVisitMoments(userId, friendId));
    }
}
