package com.cloud.demo.controller;

import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.service.api.IUserLoginLogService;
import com.cloud.demo.vo.Result;
import com.cloud.demo.vo.UserLoginLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author weiwei
 * @Date 2022/7/2 下午2:40
 * @Version 1.0
 * @Desc
 */
@RequestMapping("log")
@RestController
public class UserLogController {
    @Autowired
    private IUserLoginLogService userLoginLogService;

    @GetMapping("newest")
    public Result<UserLoginLogVo> newest(@RequestParam("accountId") Long accountId, @RequestParam("clientType") String clientType) {
        UserLoginLogVo loginLogVo = userLoginLogService.getUserNewestLogin(accountId, clientType);
        if (loginLogVo != null) {
            return Result.success(loginLogVo);
        }
        return Result.fail(CommonExceptionEnum.DATA_NOT_FOUND);
    }

    @PostMapping("log")
    public Result<Boolean> insertLog(@RequestBody UserLoginLogVo loginLogVo) {
        userLoginLogService.insert(loginLogVo);
        return Result.success(true);
    }
}
