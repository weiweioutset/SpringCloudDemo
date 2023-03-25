package com.cloud.demo.controller;

import com.cloud.demo.service.api.IdGeneratorClient;
import com.cloud.demo.service.api.IUserDetailService;
import com.cloud.demo.vo.Result;
import com.cloud.demo.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author weiwei
 * @Date 2022/5/8 上午11:28
 * @Version 1.0
 * @Desc 用于授权服务获取用户信息
 */
@RestController
@RequestMapping("detail")
public class UserDetailController {
    @Autowired
    private IUserDetailService userDetailService;
    @Autowired
    private IdGeneratorClient idGeneratorClient;

    @GetMapping("forAuth/{accountId}")
    public UserVo userDetailForAuth(@PathVariable("accountId") String accountId) {
        return userDetailService.getUserForAuth(accountId);
    }

    @GetMapping("{accountId}")
    public UserVo userDetail(@PathVariable("accountId") Long accountId) {
        return userDetailService.getByAccountId(accountId);
    }

    @GetMapping("phone/{phone}")
    public UserVo userDetailByPhone(@PathVariable("phone") String phone) {
        return userDetailService.getByPhone(phone);
    }

    @GetMapping("id")
    public Result<Long> testGetId(@RequestParam("token") String token,@RequestParam("bizType") String bizType) {
        return idGeneratorClient.nextId(bizType, token);
    }
}
