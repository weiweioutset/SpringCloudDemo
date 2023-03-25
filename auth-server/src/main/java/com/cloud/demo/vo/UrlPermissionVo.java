package com.cloud.demo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/7/4 下午9:37
 * @Version 1.0
 * @Desc
 */
@Data
@NoArgsConstructor
public class UrlPermissionVo {
    private String antPath;
    private List<String> permissions;
}
