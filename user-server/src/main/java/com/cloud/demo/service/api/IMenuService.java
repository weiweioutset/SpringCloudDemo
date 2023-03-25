package com.cloud.demo.service.api;

import com.cloud.demo.vo.MenuInfoVo;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/7/17 下午4:02
 * @Version 1.0
 * @Desc
 */
public interface IMenuService {
    /**
     * 获取所有的菜单
     * @return
     */
    List<MenuInfoVo> getAllMenu();

    /**
     * 新增菜单
     * @param menuInfoVo
     * @return
     */
    boolean addMenu(MenuInfoVo menuInfoVo);

    /**
     * 更新菜单
     * @param menuInfoVo
     * @return
     */
    boolean updateMenu(MenuInfoVo menuInfoVo);

    /**
     * 删除菜单
     * @param id 菜单id
     * @return
     */
    boolean deleteMenu(long id);

    /**
     * 给角色增加菜单权限
     * @param roleCode 角色代码
     * @param menuCodes 菜单代码，逗号隔开
     * @return
     */
    int addMenu2Role(String roleCode, String menuCodes);

    /**
     * 将菜单加载进权限表里面，这样网关服务才能进行正确的鉴权
     * 注意，每次菜单的变动都需要重新加载
     * @return
     */
    int loadMenu2AuthorityMap();
}
