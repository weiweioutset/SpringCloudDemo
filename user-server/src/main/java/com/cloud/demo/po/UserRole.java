package com.cloud.demo.po;

import com.baomidou.mybatisplus.annotation.*;
import com.cloud.demo.vo.UserRoleVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/5/11 下午10:33
 * @Version 1.0
 * @Desc
 */
@Data
@NoArgsConstructor
@TableName("role_info")
public class UserRole {
    public UserRole(UserRoleVo userRoleVo) {
        BeanUtils.copyProperties(userRoleVo, this);
    }
    /**
     * 唯一主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 角色代码
     */
    @TableField("role_code")
    private String roleCode;
    /**
     * 角色名
     */
    @TableField("role_name")
    private String roleName;
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
     * 创建人
     */
    @TableField("create_user")
    private Long createUser;
    /**
     * 更新人
     */
    @TableField("update_user")
    private Long updateUser;
}
