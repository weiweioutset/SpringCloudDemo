package com.cloud.demo.controller;

import com.cloud.demo.utils.LoginInfoUtil;
import com.cloud.demo.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author weiwei
 * @Date 2022/7/10 下午3:04
 * @Version 1.0
 * @Desc
 */
@RestController
public class BaseController {
    @Autowired
    private LoginInfoUtil loginInfoUtil;

    /**
     * 获取用户信息
     * @return
     */
    public UserVo userInfo() {
        return loginInfoUtil.userInfo();
    }
}
