package com.cloud.demo.controller;

import com.cloud.demo.enums.FileNameGenerateStrategy;
import com.cloud.demo.service.client.IUserServerClient;
import com.cloud.demo.utils.FileUtils;
import com.cloud.demo.vo.Result;
import com.cloud.demo.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/7/10 下午3:03
 * @Version 1.0
 * @Desc
 */
@RestController
@RequestMapping("test")
public class TestController extends BaseController {

    @GetMapping("info")
    public Result<UserVo> info() {
        return Result.success(userInfo());
    }

    @PostMapping("file")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String path = FileUtils.uploadImage(file);
        return Result.success(path);
    }

    @PostMapping("files")
    public Result<List<String>> uploadFiles(@RequestParam("file") MultipartFile[] files) throws IOException {
        List<String> paths = new ArrayList<>();
        for (MultipartFile file : files) {
            String path = FileUtils.uploadImage(file);
            paths.add(path);
        }
        return Result.success(paths);
    }

    @PostMapping("fileName")
    public Result<List<String>> uploadFileByName(@RequestParam("file") MultipartFile[] files) throws IOException {
        List<String> paths = new ArrayList<>();
        for (MultipartFile file : files) {
            String path = FileUtils.fileUpload(file, "image", FileNameGenerateStrategy.DATE_NAME);
            paths.add(path);
        }
        return Result.success(paths);
    }
}
