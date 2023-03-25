package com.cloud.demo.controller;

import com.cloud.demo.service.api.IMenuService;
import com.cloud.demo.vo.MenuInfoVo;
import com.cloud.demo.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/7/18 下午9:10
 * @Version 1.0
 * @Desc
 */
@RestController
@RequestMapping("menu")
public class MenuController {
    @Autowired
    private IMenuService menuService;

    /**
     * 获取所有菜单(树形结构)
     * @return
     */
    @GetMapping("list")
    public Result<List<MenuInfoVo>> listAllMenu() {
        return Result.success(menuService.getAllMenu());
    }

    /**
     * 添加菜单
     * @param menuInfoVo
     * @return
     */
    @PostMapping("add")
    public Result<Boolean> add(@RequestBody @Validated MenuInfoVo menuInfoVo) {
        return Result.success(menuService.addMenu(menuInfoVo));
    }

    /**
     * 删除菜单
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    public Result<Boolean> delete(@RequestParam("id") Long id) {
        return Result.success(menuService.deleteMenu(id));
    }

    /**
     * 菜单关联到角色
     * @param roleCode
     * @param menuCodes
     * @return
     */
    @PostMapping("relation")
    public Result<Integer> relation(@RequestParam("roleCode") String roleCode, @RequestParam("menuCodes") String menuCodes) {
        return Result.success(menuService.addMenu2Role(roleCode, menuCodes));
    }

    /**
     * 重新加载菜单权限至权限表
     * @return
     */
    @PostMapping("reload")
    public Result<Integer> reload() {
        return Result.success(menuService.loadMenu2AuthorityMap());
    }
}
