package com.cloud.demo.vo;

import com.cloud.demo.constant.BaseConstant;
import com.cloud.demo.po.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.util.Date;
import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/4/22 下午11:05
 * @Version 1.0
 * @Desc
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVo {
    public UserVo(User user) {
        BeanUtils.copyProperties(user, this,"password");
    }
    /**
     * 账号
     */
    private Long accountId;
    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    private String userName;
    /**
     * 英文名
     */
    private String englishName;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 注册时间
     */
    @JsonFormat(pattern = BaseConstant.PATTERN_A, timezone = BaseConstant.TIME_ZONE)
    private Date registerTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = BaseConstant.PATTERN_A, timezone = BaseConstant.TIME_ZONE)
    private Date updateTime;
    /**
     * 电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 密码（加密）
     */
    @NotEmpty(message = "密码不能为空")
    private String password;
    /**
     * 生日
     */
    @Past(message = "生日不合法")
    @JsonFormat(pattern = BaseConstant.PATTERN_C, timezone = BaseConstant.TIME_ZONE)
    private Date birthday;
    /**
     * 账号状态 0 申请中 1有效 2删除 3封号
     */
    private Integer status;
    /**
     * 头像地址
     */
    private String avatar;
    /**
     * 头像缩略图地址
     */
    private String thumbnailAvatar;
    /**
     * 角色列表
     */
    private List<String> roles;
}
