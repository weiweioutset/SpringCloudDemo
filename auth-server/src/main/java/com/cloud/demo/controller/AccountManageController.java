package com.cloud.demo.controller;

import com.cloud.demo.service.IAccountManageService;
import com.cloud.demo.vo.OnlineUserVo;
import com.cloud.demo.vo.Page;
import com.cloud.demo.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author weiwei
 * @Date 2022/7/2 下午3:00
 * @Version 1.0
 * @Desc 后台用户管理接口
 */
@RestController
@RequestMapping("/manage/user")
public class AccountManageController {
    @Autowired
    private IAccountManageService accountManageService;

    /**
     * 在线用户数量
     * @return
     */
    @GetMapping("/onlineNum")
    public Result<Integer> onlineNum() {
        return Result.success(accountManageService.countOnlineUser());
    }

    /**
     * 将用户强制下线
     * @param accountId
     * @param clientType
     * @return
     */
    @PostMapping("/offline")
    public Result<Integer> offlineUser(Long accountId, String clientType) {
        return Result.success(accountManageService.offlineUser(accountId, clientType));
    }

    /**
     * 在线用户信息
     * @param page
     * @return
     */
    @GetMapping("onlineUser")
    public Result<Page<OnlineUserVo>> onlineUser(Page<OnlineUserVo> page) {
        accountManageService.onlineUser(page);
        return Result.success(page);
    }
}
