package com.cloud.demo.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/7/17 下午4:10
 * @Version 1.0
 * @Desc
 */
@Data
@NoArgsConstructor
@TableName("role_menu_relation")
public class RoleMenuRelation {
    /**
     * 角色id
     */
    @TableField("role_id")
    private Long roleId;
    /**
     * 菜单id
     */
    @TableField("menu_id")
    private Long menuId;
    /**
     * 创建人
     */
    @TableField("create_user")
    private Long createUser;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    private Date createTime;
}
