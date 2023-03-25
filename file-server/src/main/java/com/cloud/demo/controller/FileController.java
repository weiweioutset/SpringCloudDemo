package com.cloud.demo.controller;

import com.cloud.demo.service.api.IFileService;
import com.cloud.demo.vo.CommonProgressVo;
import com.cloud.demo.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author weiwei
 * @Date 2022/8/16 下午10:50
 * @Version 1.0
 * @Desc
 */
@RestController
@RequestMapping("upload")
public class FileController {
    @Autowired
    private IFileService fileService;

    @PostMapping("upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        return Result.success(fileService.upload(file));
    }

    @PostMapping("blockUpload")
    public Result<String> blockUpload(@RequestParam("file") MultipartFile file) throws IOException {
        return Result.success(fileService.blockUpload(file));
    }

    @GetMapping("upload/progress")
    public Result<CommonProgressVo> uploadProgress(@RequestParam("uploadId") String uploadId) {
        return Result.success(fileService.uploadProgress(uploadId));
    }

    @GetMapping("upload/cancel")
    public Result cancelUpload(@RequestParam("uploadId") String uploadId) {
        fileService.cancelUpload(uploadId);
        return Result.success();
    }

    @GetMapping("upload/pause")
    public Result pauseUpload(@RequestParam("uploadId") String uploadId) {
        fileService.pauseUpload(uploadId);
        return Result.success();
    }

    @GetMapping("upload/resume")
    public Result resumeUpload(@RequestParam("uploadId") String uploadId) {
        fileService.resumeUpload(uploadId);
        return Result.success();
    }
}
