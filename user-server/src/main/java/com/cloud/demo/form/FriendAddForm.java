package com.cloud.demo.form;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author weiwei
 * @Date 2022/7/21 下午10:23
 * @Version 1.0
 * @Desc 添加好友请求体
 */
@Data
@NoArgsConstructor
public class FriendAddForm {
    /**
     * 好友id
     */
    @NotNull(message = "好友id不能为空")
    private Long friendId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否仅聊天
     */
    private Boolean chatsOnly = false;
    /**
     * 不让他看我动态
     */
    private Boolean hideMyPosts = false;
    /**
     * 不看他动态
     */
    private Boolean hideHisPosts = false;
    /**
     * 添加来源
     * @see com.cloud.demo.enums.FromSourceEnums
     */
    @NotEmpty(message = "添加来源不能为空")
    private String fromSource;
    /**
     * 申请信息
     */
    private String applyMessage;
    /**
     * 是否同意申请
     */
    private Boolean agreeApply = true;
}
