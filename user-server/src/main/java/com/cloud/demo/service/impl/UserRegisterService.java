package com.cloud.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.demo.enums.RoleEnums;
import com.cloud.demo.common.UserBaseConstant;
import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.exception.CommonException;
import com.cloud.demo.mapper.UserMapper;
import com.cloud.demo.po.User;
import com.cloud.demo.service.api.IUserRoleRelationService;
import com.cloud.demo.service.api.IUserRoleService;
import com.cloud.demo.service.api.IdGeneratorClient;
import com.cloud.demo.service.api.IUserRegisterService;
import com.cloud.demo.utils.NumberUtil;
import com.cloud.demo.utils.RSAUtils;
import com.cloud.demo.utils.RedisUtils;
import com.cloud.demo.utils.SecurityUtils;
import com.cloud.demo.vo.Result;
import com.cloud.demo.vo.UserRoleRelationVo;
import com.cloud.demo.vo.UserRoleVo;
import com.cloud.demo.vo.UserVo;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.Objects;

/**
 * @Author weiwei
 * @Date 2022/4/22 下午9:29
 * @Version 1.0
 * @Desc 用户注册服务
 */
@Service
public class UserRegisterService extends ServiceImpl<UserMapper, User> implements IUserRegisterService {
    private final Logger LOGGER = LoggerFactory.getLogger(UserRegisterService.class);
    @Autowired
    private IUserRoleRelationService userRoleRelationService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private IdGeneratorClient idGeneratorClient;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 账号注册
     * @param userVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVo register(UserVo userVo) {
        // 检查基本信息
        Assert.notNull(userVo, "参数错误");

        Date current = new Date();
        User user = new User(userVo);
        // 对前端加密后的密码进行解密
        String password;
        try {
            password = RSAUtils.privateKeyDecrypt(user.getPassword());
            // 判断密码强度
            boolean isStrongPassword = SecurityUtils.isStrongPassword(password);
            if (!isStrongPassword) {
                LOGGER.info("密码过于简单");
                throw new CommonException(CommonExceptionEnum.PASSWORD_INVALID);
            }
            // 然后再使用私钥加密
            String encryptPwd = RSAUtils.privateKeyEncrypt(password);
            user.setPassword(encryptPwd);
        } catch (Exception e) {
            throw new CommonException(CommonExceptionEnum.SYSTEM_ERROR);
        }
        // 校验密码完毕，开始生成账号并保存信息
        // 生成账号Id
        Long accountId = generateAccountId();
        userVo.setAccountId(accountId);

        // 先检查账号是否已经存在
        User oldUser = this.getOne(new QueryWrapper<User>()
                .eq("account_id", userVo.getAccountId()));
        if (!Objects.isNull(oldUser)) {
            LOGGER.info("Account[{}] already exists", userVo.getAccountId());
            throw new CommonException(CommonExceptionEnum.ACCOUNT_ALREADY_EXIST);
        }
        // 基础信息
        user.setRegisterTime(current);
        user.setUpdateTime(current);
        user.setStatus(1);
        UserVo result = new UserVo(user);
        user.setPhone(SecurityUtils.DESEncrypt(user.getPhone()));
        user.setEmail(SecurityUtils.DESEncrypt(user.getEmail()));
        // 保存实体信息
        this.save(user);

        // 获取角色
        UserRoleVo roleVo = userRoleService.getByCode(RoleEnums.ROLE_MEMBER.getRoleCode());
        if (Objects.isNull(roleVo)) {
            throw new CommonException(CommonExceptionEnum.ROLE_NOT_EXISTS);
        }
        // 保存用户角色关系
        UserRoleRelationVo roleRelationVo = new UserRoleRelationVo();
        roleRelationVo.setUserId(accountId);
        roleRelationVo.setRoleId(roleVo.getId());
        roleRelationVo.setCreateUser(accountId);
        roleRelationVo.setCreateTime(current);
        userRoleRelationService.insert(roleRelationVo);
        // todo 创建Feed信息流时间轴，添加团队欢迎动态(利用消息队列异步处理)

        // todo 发送注册成功消息，MQ发送

        return result;
    }

    /**
     * 生成账号Id
     * @return
     */
    private Long generateAccountId() {
        // 生成账号Id
        Result<Long> accountIdResponse = idGeneratorClient.nextId(UserBaseConstant.ACCOUNT_BIZ_TYPE, UserBaseConstant.ACCOUNT_TOKEN);
        if (accountIdResponse.getCode() != HttpStatus.SC_OK || Objects.isNull(accountIdResponse.getData())) {
            LOGGER.info("账号Id生成错误:{}", accountIdResponse.getMessage());
            throw new CommonException(CommonExceptionEnum.ERROR_GENERATOR_ACCOUNT_ID);
        }
        Long accountId = accountIdResponse.getData();
        if (!canCreate(accountId)) {
            LOGGER.info("特殊账号，需要预留[{}]", accountId);
            return generateAccountId();
        }
        return accountId;
    }

    /**
     * 是否能生成账号id，对于特殊账号，需要做预留处理
     * @param accountId
     * @return
     */
    private boolean canCreate(Long accountId) {
        if (Objects.isNull(accountId)) {
            return false;
        }
        return !NumberUtil.isSpecialNumber(accountId);
    }
}
