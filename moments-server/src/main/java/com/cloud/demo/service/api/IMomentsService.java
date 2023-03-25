package com.cloud.demo.service.api;

import com.cloud.demo.form.MomentsForm;
import com.cloud.demo.vo.MomentsVo;
import com.cloud.demo.vo.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/7/28 下午9:21
 * @Version 1.0
 * @Desc
 */
public interface IMomentsService {

    /**
     * 创建动态
     * @param momentsForm 动态内容
     * @param files 图片/视频等文件
     * @return
     */
    MomentsVo addMoments(MomentsForm momentsForm, MultipartFile[] files);

    /**
     * 根据id获取动态
     * @param id
     * @return
     */
    MomentsVo getById(long id);

    /**
     * 获取动态列表(朋友动态，去掉无权查看和不想查看的动态)
     * @return
     */
    List<MomentsVo> listMoments(Page<MomentsVo> page);


}
