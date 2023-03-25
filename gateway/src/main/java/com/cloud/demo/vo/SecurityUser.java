package com.cloud.demo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @Author weiwei
 * @Date 2022/6/26 下午2:08
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
        this.setEnabled(Integer.valueOf(1).equals(userVo.getStatus()));
        this.setPassword(userVo.getPassword());
        this.setStatus(userVo.getStatus());
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
        return !Integer.valueOf(0).equals(status);
    }

    @Override
    public boolean isAccountNonLocked() {
        return !Integer.valueOf(3).equals(status);
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
