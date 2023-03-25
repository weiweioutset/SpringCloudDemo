package com.cloud.demo.enums;

/**
 * @Author weiwei
 * @Date 2022/4/22 下午11:02
 * @Version 1.0
 * @Desc 通用错误枚举类
 * 3开头：账号相关异常
 * 4开头：数据相关异常
 * 5开头：系统异常
 * 6开头：文件系统异常
 * 7开头：用户服务业务异常
 */
public enum CommonExceptionEnum {
    // ====== 账号相关 ======
    ACCOUNT_ERROR(30000, "账号错误"),
    PASSWORD_INVALID(30001, "密码不符合要求"),
    PASSWORD_ERROR(30002, "密码错误"),
    INVALID_TOKEN(30003, "Token错误"),
    TOKEN_EXPIRED(30004, "Token过期"),
    ACCOUNT_ALREADY_EXIST(30005, "账号已存在"),
    ACCOUNT_NOT_EXIST(30006, "账号不存在"),
    ACCOUNT_NOT_ENABLE(30007, "账号未生效"),
    ACCOUNT_DELETED(30008, "账号已被删除"),
    ACCOUNT_BE_BLOCKED(30009, "账号已被冻结"),
    LOGIN_ERROR(30010, "登录失败"),
    NO_LOGIN(30011, "账号未登录"),
    NO_LOGIN_OR_TOKEN_INVALID(30012, "账号未登录或Token失效"),
    PHONE_HAS_BEEN_REGISTER(30013, "手机号已经注册"),
    ERROR_GENERATOR_ACCOUNT_ID(30014, "账号Id生成错误"),
    ROLE_NOT_EXISTS(30015, "角色不存在"),
    AUTHORITY_ERROR(30016, "暂无权限"),
    NO_SUCH_CLIENT(30017, "客户端类型错误"),
    INVALID_REFRESH_TOKEN(30018, "RefreshToken错误"),
    EXPIRED_REFRESH_TOKEN(30019, "RefreshToken过期，请重新登录"),
    REGISTER_FREQUENTLY(30020, "请【{expireTime}】后再次注册"),

    // ====== 数据相关 ======
    DATA_NOT_FOUND(40001, "数据为空"),
    DATA_ALREADY_EXIST(40002, "数据已存在"),
    RESOURCE_NOT_FOUND(40004, "资源不存在"),

    // ====== 系统错误 =====
    SYSTEM_ERROR(50000, "系统错误"),
    SYSTEM_BUSY(50001, "系统繁忙"),
    UNKNOWN_ERROR(50002, "未知错误"),
    INVALID_PARAMS(50003, "参数错误"),
    TIMEOUT_EXCEPTION(50004, "请求超时"),
    REPEAT_REQUEST(50005, "请勿重复请求"),
    SERVICE_UNAVAILABLE(50006, "服务不可用"),

    // ====== 文件系统错误 =====
    FILE_SYS_NOT_INIT(60000, "文件系统未初始化"),
    FILE_DIR_NOT_EXIST(60001, "文件夹不存在"),
    FILE_REDUCE_ERROR(60002, "文件压缩失败"),
    DIRECTORY_CAN_NOT_BE_NULL(60003, "文件夹不能为空"),
    FILE_NOT_EXIST(60004, "文件不存在"),
    MAKE_DIRECTORY_FAIL(60005, "新建文件夹失败"),
    FILENAME_CAN_NOT_BE_NULL(60006, "文件名不能为空"),
    FILE_ALREADY_EXIST(60007, "文件已存在"),
    FILE_UPLOAD_FAIL(60008, "文件上传失败"),
    FILE_TYPE_NOT_SUPPORT(60009, "文件类型不支持"),

    // ====== 具体业务相关 ======
    // ====== user-server ======
    ADD_FRIEND_FAIL(70000, "添加好友失败"),
    NON_FRIEND_RELATIONSHIP(70001, "非好友关系"),
    NO_FRIEND_APPLY(70002, "未收到好友申请"),
    ALREADY_FRIEND_RELATIONSHIP(70003, "已经是好友了"),

    // ====== moments-server ======
    CREATE_MOMENTS_ERROR(80000, "创建动态失败")
    ;

    CommonExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 错误码
     */
    private final int code;
    /**
     * 错误信息
     */
    private final String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
