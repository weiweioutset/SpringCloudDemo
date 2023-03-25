package com.cloud.demo.service.impl;

import com.alibaba.nacos.common.utils.MapUtils;
import com.cloud.demo.constant.RedisKeyConstant;
import com.cloud.demo.service.IAuthorityManageService;
import com.cloud.demo.utils.RedisUtils;
import com.cloud.demo.utils.StringUtil;
import com.cloud.demo.vo.Page;
import com.cloud.demo.vo.UrlPermissionVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author weiwei
 * @Date 2022/7/4 下午9:24
 * @Version 1.0
 * @Desc
 */
@Service
public class AuthorityManageService implements IAuthorityManageService {
    private final Logger logger = LoggerFactory.getLogger(AuthorityManageService.class);
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 设置请求接口所需权限
     * @param antPath
     * @param roles
     * @return
     */
    @Override
    public int setUrlPermission(String antPath, String roles) {
        if (StringUtil.isEmpty(antPath)) {
            logger.info("设置请求接口所需权限#地址错误");
            return 0;
        }
        if (StringUtil.isEmpty(roles)) {
            logger.info("删除接口权限【{}】", antPath);
            redisUtils.hashRemove(RedisKeyConstant.RESOURCE_ROLES_MAP, antPath);
            return 1;
        }
        List<String> roleList = StringUtil.stringToList(roles, ",");
        redisUtils.hashSet(RedisKeyConstant.RESOURCE_ROLES_MAP, antPath, roleList);
        return 1;
    }

    /**
     * 获取接口的权限
     * @param page
     * @return
     */
    @Override
    public List<UrlPermissionVo> urlPermissions(Page<UrlPermissionVo> page) {
        Map<String,Object> authorityMap = redisUtils.hashEntries(RedisKeyConstant.RESOURCE_ROLES_MAP);
        if (authorityMap == null || MapUtils.isEmpty(authorityMap)) {
            return page.getRecords();
        }

        // 处理分页信息
        long pageSize = page.getSize();
        long current = page.getCurrent();
        long jumpSize = pageSize * (current - 1);
        page.setTotal(authorityMap.keySet().size());
        List<UrlPermissionVo> result = new ArrayList<>();
        List<String> keys = authorityMap.keySet().stream()
                .skip(jumpSize).limit(pageSize)
                .collect(Collectors.toList());
        for (String key : keys) {
            String roles = authorityMap.get(key).toString();
            List<String> permissions = StringUtil.listStringToList(roles);
            UrlPermissionVo permissionVo = new UrlPermissionVo();
            permissionVo.setAntPath(key);
            permissionVo.setPermissions(permissions);
            result.add(permissionVo);
        }
        page.setRecords(result);
        return result;
    }
}
