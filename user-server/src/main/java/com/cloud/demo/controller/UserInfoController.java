package com.cloud.demo.controller;

import com.cloud.demo.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author weiwei
 * @Date 2022/6/24 下午8:09
 * @Version 1.0
 * @Desc 用户信息Controller，
 * 提供修改用户信息，重置密码等功能
 */
@RestController
@RequestMapping("info")
public class UserInfoController {

    @PostMapping("password/change")
    public Result<String> changePassword() {
        return Result.success("测试");
    }
}
