package com.cloud.demo.service.api;

import com.cloud.demo.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author weiwei
 * @Date 2022/6/18 下午4:18
 * @Version 1.0
 * @Desc Id生成远程调用客户端
 */
@FeignClient(name = "id-generator")
public interface IdGeneratorClient {
    @GetMapping("id-generator/id/nextId")
    Result<Long> nextId(@RequestParam("bizType") String bizType, @RequestParam("token") String token);
}
