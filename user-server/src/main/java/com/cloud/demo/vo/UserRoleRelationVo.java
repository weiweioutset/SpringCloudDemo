package com.cloud.demo.vo;

import com.cloud.demo.po.UserRoleRelation;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/5/25 下午10:58
 * @Version 1.0
 * @Desc 用户角色关系表
 */
@Data
@NoArgsConstructor
public class UserRoleRelationVo {
    public UserRoleRelationVo(UserRoleRelation relation) {
        BeanUtils.copyProperties(relation, this);
    }
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private Long createUser;
}
