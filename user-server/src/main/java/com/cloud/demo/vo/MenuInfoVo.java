package com.cloud.demo.vo;

import com.cloud.demo.po.MenuInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/7/16 下午2:37
 * @Version 1.0
 * @Desc 菜单实体类
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuInfoVo {
    public MenuInfoVo(MenuInfo menuInfo) {
        BeanUtils.copyProperties(menuInfo, this);
    }

    /**
     * 唯一主键
     */
    private Long id;
    /**
     * 菜单代码
     */
    @NotEmpty(message = "菜单代码不能为空")
    private String menuCode;
    /**
     * 菜单名
     */
    @NotEmpty(message = "菜单名不能为空")
    private String menuName;
    /**
     * 菜单地址
     */
    @NotEmpty(message = "菜单地址不能为空")
    private String antUrl;
    /**
     * 父级菜单代码
     */
    private String parentCode;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建人id
     */
    private Long createUser;
    /**
     * 更新人id
     */
    private Long updateUser;
    /**
     * 子菜单
     */
    private List<MenuInfoVo> children;
}
