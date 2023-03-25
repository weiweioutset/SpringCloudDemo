package com.cloud.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.demo.constant.RedisKeyConstant;
import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.exception.CommonException;
import com.cloud.demo.mapper.MenuMapper;
import com.cloud.demo.mapper.RoleMenuRelationMapper;
import com.cloud.demo.po.MenuInfo;
import com.cloud.demo.po.RoleMenuRelation;
import com.cloud.demo.po.UserRole;
import com.cloud.demo.service.api.IMenuService;
import com.cloud.demo.service.api.IUserRoleService;
import com.cloud.demo.utils.LoginInfoUtil;
import com.cloud.demo.utils.ParseMenu2Tree;
import com.cloud.demo.utils.RedisUtils;
import com.cloud.demo.utils.StringUtil;
import com.cloud.demo.vo.MenuInfoVo;
import com.cloud.demo.vo.UserRoleVo;
import com.cloud.demo.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author weiwei
 * @Date 2022/7/17 下午4:17
 * @Version 1.0
 * @Desc 菜单服务
 */
@Service
public class MenuService extends ServiceImpl<MenuMapper, MenuInfo> implements IMenuService {
    private final Logger LOGGER = LoggerFactory.getLogger(MenuService.class);
    @Autowired
    private LoginInfoUtil loginInfoUtil;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private RoleMenuRelationMapper roleMenuRelationMapper;
    @Autowired
    private RedisUtils redisUtils;

    @Override
    public int loadMenu2AuthorityMap() {
        // 获取所有菜单
        List<MenuInfo> allMenus = this.list();
        if (CollectionUtils.isEmpty(allMenus)) {
            return 0;
        }
        // 将所有菜单按照接口地址分组
        Map<String, List<Long>> urlRolesMap = allMenus.stream()
                .collect(Collectors.groupingBy(MenuInfo::getAntUrl,
                        Collectors.mapping(MenuInfo::getId, Collectors.toList())));
        // 查询所有的角色
        List<UserRole> allRoles = userRoleService.listAllRoles();
        // 将角色按照id和code分组
        Map<Long, String> roleMap = allRoles.stream()
                .collect(Collectors.toMap(UserRole::getId, UserRole::getRoleCode));
        // 查询所有的菜单角色关系
        List<RoleMenuRelation> relations = roleMenuRelationMapper.selectList(Wrappers.emptyWrapper());
        // 将角色和菜单关系分组
        Map<Long, Long> relationMap = relations.stream()
                .collect(Collectors.toMap(RoleMenuRelation::getMenuId, RoleMenuRelation::getRoleId));

        // 删除所有的权限，重新加载权限
        redisUtils.remove(RedisKeyConstant.RESOURCE_ROLES_MAP);
        int successCount = 0;
        // 查询每个接口地址对应的角色
        for (String antPath : urlRolesMap.keySet()) {
            List<String> roles = new ArrayList<>();
            List<Long> menus = urlRolesMap.get(antPath);
            if (CollectionUtils.isEmpty(menus)) {
                continue;
            }
            for (Long menuId : menus) {
                Long roleId = relationMap.get(menuId);
                if (Objects.isNull(roleId)) {
                    continue;
                }
                String roleCode = roleMap.get(roleId);
                if (!StringUtil.isEmpty(roleCode)) {
                    roles.add(roleCode);
                }
            }

            if (!CollectionUtils.isEmpty(roles)) {
                successCount++;
                redisUtils.hashSet(RedisKeyConstant.RESOURCE_ROLES_MAP, antPath, roles);
            }
        }

        return successCount;
    }

    @Override
    public List<MenuInfoVo> getAllMenu() {
        // 获取所有菜单
        List<MenuInfo> allMenus = this.list();
        // 类型转换
        List<MenuInfoVo> allMenuVos = new ArrayList<>();
        for (MenuInfo menu : allMenus) {
            MenuInfoVo menuInfoVo = new MenuInfoVo(menu);
            allMenuVos.add(menuInfoVo);
        }
        // 建立树形结构
        return ParseMenu2Tree.buildTree(allMenuVos);
    }

    @Override
    public boolean addMenu(MenuInfoVo menuInfoVo) {
        // 检查菜单代码是否已经存在
        MenuInfo existMenu = this.getOne(new QueryWrapper<MenuInfo>().eq("menu_code",
                menuInfoVo.getMenuCode()));
        if (!Objects.isNull(existMenu)) {
            LOGGER.info("菜单已存在[{}]", menuInfoVo.getMenuCode());
            throw new CommonException(CommonExceptionEnum.DATA_ALREADY_EXIST);
        }
        // 设置创建人信息
        UserVo createUser = loginInfoUtil.userInfo();
        Date current = new Date();
        menuInfoVo.setCreateTime(current);
        menuInfoVo.setCreateUser(createUser.getAccountId());
        menuInfoVo.setUpdateTime(current);
        menuInfoVo.setUpdateUser(createUser.getAccountId());
        // 转换为实体类
        MenuInfo menuInfo = new MenuInfo(menuInfoVo);
        // 保存菜单
        return this.save(menuInfo);
    }

    @Override
    public boolean updateMenu(MenuInfoVo menuInfoVo) {
        // 设置创建人信息
        UserVo createUser = loginInfoUtil.userInfo();
        // 只可更新地址和名字等信息，代码不可变更
        MenuInfo oldMenu = this.getById(menuInfoVo.getId());
        if (oldMenu == null) {
            LOGGER.info("菜单不存在{}", menuInfoVo.getId());
            throw new CommonException(CommonExceptionEnum.DATA_NOT_FOUND);
        }
        String oldPath = oldMenu.getAntUrl();
        oldMenu.setUpdateTime(new Date());
        oldMenu.setUpdateUser(createUser.getAccountId());
        oldMenu.setAntUrl(menuInfoVo.getAntUrl());
        oldMenu.setMenuName(menuInfoVo.getMenuName());
        oldMenu.setParentCode(menuInfoVo.getParentCode());
        // 更新
        this.updateById(oldMenu);
        // 如果是地址变动的话，需要刷新权限
        if (!oldPath.equals(menuInfoVo.getAntUrl())) {
            loadMenu2AuthorityMap();
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMenu(long id) {
        if (id <= 0) {
            return false;
        }
        // 删除菜单
        boolean deleteSuccess = this.removeById(id);
        if (deleteSuccess) {
            // 删除菜单成功之后需要同步删除菜单关系
            roleMenuRelationMapper.deleteByMenuId(id);
        }
        // 刷新权限
        loadMenu2AuthorityMap();
        return deleteSuccess;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addMenu2Role(String roleCode, String menuCodes) {
        // 设置创建人信息
        UserVo createUser = loginInfoUtil.userInfo();
        // 判断是否存在该角色
        UserRoleVo roleVo = userRoleService.getByCode(roleCode);
        if (roleVo == null) {
            LOGGER.info("角色不存在{}", roleCode);
            throw new CommonException(CommonExceptionEnum.DATA_NOT_FOUND);
        }
        // 获取所有的菜单
        List<String> menuCodeList = StringUtil.stringToList(menuCodes, ",");
        Collection<MenuInfo> menus = this.list(new QueryWrapper<MenuInfo>().in("menu_code", menuCodeList));
        // 如果代码数量和实际菜单数量不一致，就表示菜单缺失
        if (menus.size() < menuCodeList.size()) {
            LOGGER.info("菜单缺失[{}]", menuCodes);
            throw new CommonException(CommonExceptionEnum.DATA_NOT_FOUND);
        }
        Date current = new Date();
        // 删除原有的关系
        roleMenuRelationMapper.deleteByRoleId(roleVo.getId());
        // 添加菜单和角色之间的关系
        for (MenuInfo menuInfo : menus) {
            RoleMenuRelation relation = new RoleMenuRelation();
            relation.setMenuId(menuInfo.getId());
            relation.setRoleId(roleVo.getId());
            relation.setCreateUser(createUser.getAccountId());
            relation.setCreateTime(current);
            roleMenuRelationMapper.addRoleRelation(relation);
        }
        // 刷新权限
        loadMenu2AuthorityMap();
        return menus.size();
    }
}
