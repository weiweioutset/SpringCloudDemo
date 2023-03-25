package com.cloud.demo.vo;

import com.cloud.demo.constant.BaseConstant;
import com.cloud.demo.po.Friend;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/7/21 下午9:54
 * @Version 1.0
 * @Desc 好友包装类
 */
@Data
@NoArgsConstructor
public class FriendVo {
    public FriendVo(Friend friend) {
        BeanUtils.copyProperties(friend, this);
    }
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 好友id
     */
    private Long friendId;
    /**
     * 好友详细信息
     */
    @JsonIgnoreProperties(value = {"registerTime", "updateTime", "password", "status", "roles"})
    private UserVo friendInfo;
    /**
     * 头像地址(好友列表用)
     */
    private String avatarUrl;
    /**
     * 昵称(好友列表用)
     */
    private String nickName;
    /**
     * 好友添加时间
     */
    @JsonFormat(pattern = BaseConstant.PATTERN_A, timezone = BaseConstant.TIME_ZONE)
    private Date createTime;
    /**
     * 好友关系更新时间
     */
    @JsonFormat(pattern = BaseConstant.PATTERN_A, timezone = BaseConstant.TIME_ZONE)
    private Date updateTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否仅聊天
     */
    private Boolean chatsOnly;
    /**
     * 不让他看我动态
     */
    private Boolean hideMyPosts;
    /**
     * 不看他动态
     */
    private Boolean hideHisPosts;
    /**
     * 添加来源
     */
    private String fromSource;
    /**
     * 是否添加星标
     */
    private Boolean focus;
    /**
     * 是否加入黑名单
     */
    private Boolean block;
    /**
     * 好友申请状态 0申请中 1通过 2拒绝
     */
    private Integer applyStatus;
}
