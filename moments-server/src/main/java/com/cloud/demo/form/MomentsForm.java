package com.cloud.demo.form;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/7/30 下午2:47
 * @Version 1.0
 * @Desc 动态的表单数据
 */
@Data
@NoArgsConstructor
public class MomentsForm {
    /**
     * 动态id
     */
    private Long id;
    /**
     * 动态内容
     */
    private String content;
    /**
     * 动态类型 0图文
     */
    private Integer type = 0;
    /**
     * 额外内容(保留字段)
     */
    private String extraData;
    /**
     * 可见范围:All公开/Private私密/Include部分可见/Except部分不可见
     * @see com.cloud.demo.enums.PublishScopeEnum
     */
    private String publishScope = "All";
    /**
     * 是否需要提醒谁看
     */
    private Boolean needRemind = false;
    /**
     * 需要提醒的用户id
     */
    private List<Long> remindUsers;
    /**
     * 显示地址
     */
    private String showAddress;
    /**
     * 图片/视频地址
     */
    private List<MultipartFile> mediaFiles;
}
