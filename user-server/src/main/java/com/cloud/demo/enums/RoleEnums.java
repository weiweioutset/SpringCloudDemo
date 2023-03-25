package com.cloud.demo.enums;

/**
 * @Author weiwei
 * @Date 2022/6/19 下午3:52
 * @Version 1.0
 * @Desc 角色枚举类
 */
public enum RoleEnums {
    ROLE_MEMBER("ROLE_MEMBER", "普通成员"),
    ROLE_SYSTEM_ADMIN("ROLE_SYSTEM_ADMIN", "系统管理员")
    ;

    RoleEnums(String roleCode, String roleDesc) {
        this.roleCode = roleCode;
        this.roleDesc = roleDesc;
    }

    /**
     * 角色代码
     */
    private String roleCode;
    /**
     * 角色描述
     */
    private String roleDesc;

    public String getRoleCode() {
        return roleCode;
    }

    public String getRoleDesc() {
        return roleDesc;
    }
}
