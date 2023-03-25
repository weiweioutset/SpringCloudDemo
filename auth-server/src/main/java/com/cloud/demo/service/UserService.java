package com.cloud.demo.service;

import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.exception.CommonException;
import com.cloud.demo.vo.SecurityUser;
import com.cloud.demo.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Author weiwei
 * @Date 2022/5/8 下午10:42
 * @Version 1.0
 * @Desc
 */
@Service
public class UserService implements UserDetailsService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private IUserClient userDetailClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVo userVo = userDetailClient.getUserByAccountId(username);
        if (Objects.isNull(userVo) || userVo.getAccountId() == null) {
            LOGGER.info("用户{}未注册", username);
            throw new CommonException(CommonExceptionEnum.ACCOUNT_NOT_EXIST);
        }
        SecurityUser securityUser = new SecurityUser(userVo);
        if (!securityUser.isEnabled()) {
            throw new CommonException(CommonExceptionEnum.ACCOUNT_NOT_ENABLE);
        } else if (!securityUser.isAccountNonLocked()) {
            throw new CommonException(CommonExceptionEnum.ACCOUNT_BE_BLOCKED);
        } else if (!securityUser.isAccountNonExpired()) {
            throw new CommonException(CommonExceptionEnum.ACCOUNT_NOT_ENABLE);
        } else if (!securityUser.isCredentialsNonExpired()) {
            throw new CommonException(CommonExceptionEnum.NO_LOGIN);
        }
        return securityUser;
    }
}

