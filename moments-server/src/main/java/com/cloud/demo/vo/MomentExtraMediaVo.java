package com.cloud.demo.vo;

import com.cloud.demo.constant.BaseConstant;
import com.cloud.demo.po.MomentExtraMedia;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @Author weiwei
 * @Date 2022/8/13 上午11:52
 * @Version 1.0
 * @Desc
 */
@Data
@NoArgsConstructor
public class MomentExtraMediaVo {
    public MomentExtraMediaVo(MomentExtraMedia media) {
        BeanUtils.copyProperties(media, this);
    }
    /**
     * 图片/视频地址
     */
    private String mediaPath;
    /**
     * 图片缩略地址
     */
    private String thumbnail;
    /**
     * 排序
     */
    private Integer index;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = BaseConstant.PATTERN_A, timezone = BaseConstant.TIME_ZONE)
    private Date createTime;
}
