package com.cloud.demo.service.client;

import com.cloud.demo.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/8/8 下午10:10
 * @Version 1.0
 * @Desc
 */
@FeignClient(name = "user-server")
public interface IUserServerClient {

    /**
     * 获取所有有权可见自己动态的好友列表
     * @param userId
     * @return
     */
    @GetMapping("user/friend/shouldMomentFriends")
    Result<List<Long>> listShouldShowMomentFriends(@RequestParam("userId") long userId);

    /**
     * 获取所有有权可见自己动态的好友列表
     * @param userId
     * @return
     */
    @GetMapping("user/friend/canVisitMoments")
    Result<Boolean> canVisitMoments(@RequestParam("userId") long userId,
                                       @RequestParam("friendId") long friendId);
}
