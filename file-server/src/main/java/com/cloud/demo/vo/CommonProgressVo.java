package com.cloud.demo.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author weiwei
 * @Date 2022/8/20 下午1:55
 * @Version 1.0
 * @Desc 文件下载/上传进度实体类
 */
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonProgressVo implements Serializable {
    /**
     * 关键字
     */
    private String key;
    /**
     * 总长度
     */
    private long totalByte = -1;
    /**
     * 已完成长度
     */
    private long sofarByte = -1;
    /**
     * 进度(百分比,保留两位小数)
     */
    private double percent = 0.00F;
    /**
     * 状态
     */
    private String state;
    /**
     * 是否完成
     */
    private boolean success = false;
    /**
     * 文件地址(上传文件完成后)
     */
    private String fileUrl;

    public CommonProgressVo(String key) {
        this.key = key;
    }
}
