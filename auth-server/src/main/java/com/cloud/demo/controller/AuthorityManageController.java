package com.cloud.demo.controller;

import com.cloud.demo.service.IAuthorityManageService;
import com.cloud.demo.vo.Page;
import com.cloud.demo.vo.Result;
import com.cloud.demo.vo.UrlPermissionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author weiwei
 * @Date 2022/7/4 下午9:19
 * @Version 1.0
 * @Desc 权限管理相关接口
 */
@RestController
@RequestMapping("manage/authority")
public class AuthorityManageController {
    @Autowired
    private IAuthorityManageService authorityManageService;

    /**
     * 设置url的权限
     * @return
     */
    @PostMapping("permission/url")
    public Result<Integer> urlPermission(@RequestParam("antPath") String antPath,
                               @RequestParam(value = "roles", defaultValue = "") String roles) {
        return Result.success(authorityManageService.setUrlPermission(antPath, roles));
    }

    @GetMapping("permission/url")
    public Result<Page<UrlPermissionVo>> getUrlPermissions(Page<UrlPermissionVo> page) {
        authorityManageService.urlPermissions(page);
        return Result.success(page);
    }
}
