package com.cloud.demo.service;

import com.cloud.demo.vo.Result;
import com.cloud.demo.vo.UserLoginLogVo;
import com.cloud.demo.vo.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Author weiwei
 * @Date 2022/5/9 下午8:29
 * @Version 1.0
 * @Desc
 */
@FeignClient(name = "user-server")
public interface IUserClient {
    /**
     * 获取用户详情(仅用于鉴权)
     * @param accountId
     * @return
     */
    @GetMapping("user/detail/forAuth/{accountId}")
    UserVo getUserByAccountId(@PathVariable("accountId") String accountId);

    /**
     * 获取用户详情
     * @param accountId
     * @return
     */
    @GetMapping("user/detail/{accountId}")
    UserVo getUserDetail(@PathVariable("accountId") String accountId);

    /**
     * 增加用户登录日志
     * @param loginLogVo
     * @return
     */
    @PostMapping("user/log/log")
    Result<Boolean> loginLog(@RequestBody UserLoginLogVo loginLogVo);

    /**
     * 获取用户最新一次登录日志
     * @param accountId
     * @param clientType
     * @return
     */
    @GetMapping("user/log/newest")
    Result<UserLoginLogVo> getUserNewestLogin(@RequestParam("accountId") Long accountId, @RequestParam("clientType") String clientType);
}
