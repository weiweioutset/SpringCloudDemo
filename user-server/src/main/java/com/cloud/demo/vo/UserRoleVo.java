package com.cloud.demo.vo;

import com.cloud.demo.po.UserRole;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Objects;

/**
 * @Author weiwei
 * @Date 2022/5/11 下午10:37
 * @Version 1.0
 * @Desc
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRoleVo {
    public UserRoleVo(UserRole userRole) {
        BeanUtils.copyProperties(userRole, this);
    }
    /**
     * 唯一主键
     */
    private Long id;
    /**
     * 角色代码
     */
    @NotEmpty(message = "角色代码不能为空")
    private String roleCode;
    /**
     * 角色名
     */
    private String roleName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建人
     */
    private Long createUser;
    /**
     * 更新人
     */
    private Long updateUser;
}
