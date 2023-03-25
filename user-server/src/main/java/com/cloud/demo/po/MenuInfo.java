package com.cloud.demo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.cloud.demo.vo.MenuInfoVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/7/16 下午2:31
 * @Version 1.0
 * @Desc 菜单实体类
 */
@Data
@NoArgsConstructor
@TableName("menu_info")
public class MenuInfo {

    public MenuInfo(MenuInfoVo menuInfoVo) {
        BeanUtils.copyProperties(menuInfoVo, this);
    }

    /**
     * 唯一主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 菜单代码
     */
    @TableField("menu_code")
    private String menuCode;
    /**
     * 菜单名
     */
    @TableField("menu_name")
    private String menuName;
    /**
     * 菜单地址
     */
    @TableField("ant_url")
    private String antUrl;
    /**
     * 父级菜单代码
     */
    @TableField("parent_code")
    private String parentCode;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_time")
    private Date updateTime;
    /**
     * 创建人id
     */
    @TableField("create_user")
    private Long createUser;
    /**
     * 更新人id
     */
    @TableField("update_user")
    private Long updateUser;
}
