package com.cloud.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.demo.mapper.MomentExtraMediaMapper;
import com.cloud.demo.po.MomentExtraMedia;
import com.cloud.demo.service.api.IExtraMediaService;
import org.springframework.stereotype.Service;

/**
 * @Author weiwei
 * @Date 2022/7/30 下午3:57
 * @Version 1.0
 * @Desc
 */
@Service
public class ExtraMediaService extends ServiceImpl<MomentExtraMediaMapper, MomentExtraMedia> implements IExtraMediaService {
}
