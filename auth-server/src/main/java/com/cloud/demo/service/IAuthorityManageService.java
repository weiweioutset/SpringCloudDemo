package com.cloud.demo.service;

import com.cloud.demo.vo.Page;
import com.cloud.demo.vo.UrlPermissionVo;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/7/4 下午9:24
 * @Version 1.0
 * @Desc
 */
public interface IAuthorityManageService {

    /**
     * 设置接口所需权限
     * @param antPath
     * @param roles
     * @return
     */
    int setUrlPermission(String antPath, String roles);

    /**
     * 获取所有接口的权限
     * @param page
     * @return
     */
    List<UrlPermissionVo> urlPermissions(Page<UrlPermissionVo> page);
}
