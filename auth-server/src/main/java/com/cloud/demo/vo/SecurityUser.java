package com.cloud.demo.vo;

import com.cloud.demo.enums.AccountStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @Author weiwei
 * @Date 2022/5/15 下午6:35
 * @Version 1.0
 * @Desc
 */
@Data
@NoArgsConstructor
public class SecurityUser implements UserDetails {
    /**
     * 账号
     */
    private Long accountId;
    /**
     * 用户名（账号）
     */
    private String username;
    /**
     * 姓名
     */
    private String userFullName;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户状态
     */
    private Boolean enabled;
    /**
     * 账号状态 0 申请中 1有效 2删除 3封号
     */
    private Integer status;
    /**
     * 权限数据
     */
    private Collection<SimpleGrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public SecurityUser(UserVo userVo) {
        this.setAccountId(userVo.getAccountId());
        this.setUsername(userVo.getAccountId() + "");
        this.setEnabled(AccountStatusEnum.USER_STATUS_ENABLE.getCode().equals(userVo.getStatus()));
        this.setPassword(userVo.getPassword());
        this.setStatus(userVo.getStatus());
        this.setUserFullName(userVo.getUserName());
        if (userVo.getRoles() != null) {
            authorities = new ArrayList<>();
            userVo.getRoles().forEach(item -> authorities.add(new SimpleGrantedAuthority(item)));
        }
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.accountId + "";
    }

    @Override
    public boolean isAccountNonExpired() {
        return !AccountStatusEnum.USER_STATUS_APPLY.getCode().equals(status);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !AccountStatusEnum.USER_STATUS_DELETE.getCode().equals(status);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
