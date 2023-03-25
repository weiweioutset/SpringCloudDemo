package com.cloud.demo.form;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author weiwei
 * @Date 2022/7/21 下午10:37
 * @Version 1.0
 * @Desc 更新好友信息/关系请求体
 */
@Data
@NoArgsConstructor
public class FriendUpdateForm {
    /**
     * 好友id
     */
    @NotNull(message = "好友id不能为空")
    private Long friendId;
    /**
     * 通用的值
     * 如果是更新备注，就是String，如果是更新其他权限，就是Boolean...
     */
    @NotNull(message = "请求值不能为空")
    private Object value;
}
