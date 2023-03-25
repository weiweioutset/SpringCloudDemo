package com.cloud.demo.service.api;

import com.cloud.demo.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author weiwei
 * @Date 2022/7/30 下午1:54
 * @Version 1.0
 * @Desc 分布式ID生成接口
 */
@FeignClient(name = "id-generator")
public interface IdGeneratorClient {
    @GetMapping("id-generator/id/nextId")
    Result<Long> nextId(@RequestParam("bizType") String bizType, @RequestParam("token") String token);
}