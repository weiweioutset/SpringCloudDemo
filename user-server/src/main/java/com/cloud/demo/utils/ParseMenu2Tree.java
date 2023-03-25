package com.cloud.demo.utils;

import com.cloud.demo.vo.MenuInfoVo;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author weiwei
 * @Date 2022/7/17 下午4:26
 * @Version 1.0
 * @Desc 将菜单转换为树形结构
 */
public class ParseMenu2Tree {

    /**
     * 构造树形结构
     * @param list
     * @return
     */
    public static List<MenuInfoVo> buildTree(List<MenuInfoVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<MenuInfoVo> result = new ArrayList<>();
        // 获取父节点
        List<MenuInfoVo> rootNode = getRootNode(list);
        // 遍历所有父节点
        for (MenuInfoVo menuInfoVo : rootNode) {
            // 建立子树节点
            buildChildrenNode(menuInfoVo, list);
            result.add(menuInfoVo);
        }
        return result;
    }

    /**
     * 获取全部根节点
     * @param list
     * @return
     */
    private static List<MenuInfoVo> getRootNode(List<MenuInfoVo> list) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream()
                .filter(m -> StringUtil.isEmpty(m.getParentCode()))
                .collect(Collectors.toList());
    }

    /**
     * 通过递归来创建子树形结构
     * @param menuInfoVo
     * @return
     */
    private static MenuInfoVo buildChildrenNode(MenuInfoVo menuInfoVo, List<MenuInfoVo> list) {
        List<MenuInfoVo> treeList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return menuInfoVo;
        }
        for (MenuInfoVo menu : list) {
            // 判断当前节点是否存在子节点
            if (menuInfoVo.getMenuCode().equals(menu.getParentCode())) {
                treeList.add(buildChildrenNode(menu, list));
            }
        }
        menuInfoVo.setChildren(treeList);
        return menuInfoVo;
    }
}
