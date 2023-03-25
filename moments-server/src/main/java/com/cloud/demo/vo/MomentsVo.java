package com.cloud.demo.vo;

import com.cloud.demo.constant.BaseConstant;
import com.cloud.demo.po.Moments;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/7/30 下午2:42
 * @Version 1.0
 * @Desc
 */
@Data
@NoArgsConstructor
public class MomentsVo {
    public MomentsVo (Moments moments) {
        BeanUtils.copyProperties(moments, this);
    }
    /**
     * 动态id
     */
    private Long id;
    /**
     * 动态内容
     */
    private String content;
    /**
     * 动态作者
     */
    private Long userId;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = BaseConstant.PATTERN_A, timezone = BaseConstant.TIME_ZONE)
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = BaseConstant.PATTERN_A, timezone = BaseConstant.TIME_ZONE)
    private Date updateTime;
    /**
     * 动态类型 0图文
     */
    private Integer type;
    /**
     * 是否有额外内容(如图片视频等)
     */
    private Boolean hasExtra;
    /**
     * 额外内容(保留字段)
     */
    private String extraData;
    /**
     * 可见范围:All公开/Private私密/Include部分可见/Except部分不可见
     * @see com.cloud.demo.enums.PublishScopeEnum
     */
    private String publishScope;
    /**
     * 是否需要提醒谁看
     */
    private Boolean needRemind;
    /**
     * 是否被删除
     */
    private Boolean isDelete;
    /**
     * 发布IP地址
     */
    private String publishIp;
    /**
     * 显示地址
     */
    private String showAddress;
    /**
     * 图片/视频信息
     */
    private List<MomentExtraMediaVo> mediaInfos;
}
