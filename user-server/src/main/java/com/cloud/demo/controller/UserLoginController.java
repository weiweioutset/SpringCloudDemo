package com.cloud.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.demo.service.api.IUserLoginLogService;
import com.cloud.demo.vo.Result;
import com.cloud.demo.vo.UserLoginLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/7/2 下午12:11
 * @Version 1.0
 * @Desc
 */
@RequestMapping("login")
@RestController
public class UserLoginController {
    @Autowired
    private IUserLoginLogService userLoginLogService;

    @GetMapping("testTime")
    public Result<String> testTime() throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("执行完毕");
        return Result.success(System.currentTimeMillis() + "");
    }
}


