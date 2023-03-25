package com.cloud.demo.controller;

import com.cloud.demo.service.api.IUserRegisterService;
import com.cloud.demo.vo.Result;
import com.cloud.demo.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author weiwei
 * @Date 2022/5/22 下午6:13
 * @Version 1.0
 * @Desc 用户注册Controller
 */
@RestController
@RequestMapping("register")
public class UserRegisterController {
    @Autowired
    private IUserRegisterService userRegisterService;

    @PostMapping
    public Result<UserVo> register(@RequestBody @Validated UserVo userVo) {
        UserVo resultVo = userRegisterService.register(userVo);
        return Result.success(resultVo);
    }
}
