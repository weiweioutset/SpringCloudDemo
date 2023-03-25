package com.cloud.demo.utils;

import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.exception.CommonException;
import com.cloud.demo.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Author weiwei
 * @Date 2022/7/10 下午2:54
 * @Version 1.0
 * @Desc 获取用户登录数据
 */
@Component
public class LoginInfoUtil {
    private static final String AUTHORIZE_PARAM = "access_token";
    private static final String AUTHORIZE_HEADER = "Authorization";
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取用户信息并检查是否登录
     * @return
     */
    public UserVo userInfo() {
        UserVo userVo = getUserInfo();
        if (Objects.isNull(userVo)) {
            throw new CommonException(CommonExceptionEnum.NO_LOGIN);
        }
        return userVo;
    }

    /**
     * 获取用户登录数据
     * @return
     */
    public UserVo getUserInfo() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            // 从请求头中获取token
            String token = request.getHeader(AUTHORIZE_HEADER);
            if (StringUtils.isEmpty(token)) {
                // 如果请求头中没有，就从参数中获取
                token = request.getParameter(AUTHORIZE_PARAM);
            }
            if (StringUtils.isEmpty(token)) {
                // 白名单中的接口，不需要用户信息
                return null;
            }
            String accessToken = token.replace("Bearer ", "");
            Object userObject = redisUtils.getValue(accessToken);
            if (userObject instanceof UserVo) {
                return (UserVo) userObject;
            }
        }
        return null;
    }
}