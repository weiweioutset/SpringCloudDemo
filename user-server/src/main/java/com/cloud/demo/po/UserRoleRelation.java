package com.cloud.demo.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cloud.demo.vo.UserRoleRelationVo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/5/25 下午10:55
 * @Version 1.0
 * @Desc
 */
@Data
@NoArgsConstructor
@TableName("user_role_relation")
public class UserRoleRelation {
    public UserRoleRelation(UserRoleRelationVo roleRelationVo) {
        BeanUtils.copyProperties(roleRelationVo, this);
    }
    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 角色id
     */
    @TableField("role_id")
    private Long roleId;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT, value = "create_time")
    private Date createTime;
    /**
     * 创建人
     */
    @TableField("create_user")
    private Long createUser;
}
