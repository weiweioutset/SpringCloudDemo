package com.cloud.demo.controller;

import com.cloud.demo.form.MomentsForm;
import com.cloud.demo.service.api.IMomentsService;
import com.cloud.demo.vo.MomentsVo;
import com.cloud.demo.vo.Page;
import com.cloud.demo.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * @Author weiwei
 * @Date 2022/7/30 下午2:53
 * @Version 1.0
 * @Desc 动态Controller
 */
@RestController
@RequestMapping("moment")
public class MomentsController {
    @Autowired
    private IMomentsService momentsService;

    /**
     * 创建动态
     * @param moments 动态
     * @param file 图片/视频等文件
     * @return
     */
    @PostMapping("add")
    public Result<MomentsVo> addMoments(@Validated MomentsForm moments,
                                        @RequestParam(value = "file", required = false) MultipartFile[] file) {
        return Result.success(momentsService.addMoments(moments, file));
    }

    /**
     * 分页获取动态
     * @param page
     * @return
     */
    @GetMapping("page")
    public Result<Page<MomentsVo>> page(Page<MomentsVo> page) {
        momentsService.listMoments(page);
        return Result.success(page);
    }
}
