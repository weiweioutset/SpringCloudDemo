package com.cloud.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.demo.common.MomentsBaseConstant;
import com.cloud.demo.constant.RedisKeyConstant;
import com.cloud.demo.enums.CommonExceptionEnum;
import com.cloud.demo.enums.FileNameGenerateStrategy;
import com.cloud.demo.enums.PublishScopeEnum;
import com.cloud.demo.exception.CommonException;
import com.cloud.demo.form.MomentsForm;
import com.cloud.demo.mapper.MomentsMapper;
import com.cloud.demo.po.MomentExtraMedia;
import com.cloud.demo.po.Moments;
import com.cloud.demo.service.api.IMomentsService;
import com.cloud.demo.service.api.IdGeneratorClient;
import com.cloud.demo.service.client.IUserServerClient;
import com.cloud.demo.utils.*;
import com.cloud.demo.vo.*;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author weiwei
 * @Date 2022/7/28 下午9:21
 * @Version 1.0
 * @Desc 动态Service
 */
@Service
public class MomentsService extends ServiceImpl<MomentsMapper, Moments> implements IMomentsService {
    private final Logger LOGGER = LoggerFactory.getLogger(MomentsService.class);
    @Autowired
    private IdGeneratorClient idGeneratorClient;
    @Autowired
    private LoginInfoUtil loginInfoUtil;
    @Autowired
    private ExtraMediaService extraMediaService;
    @Autowired
    private IUserServerClient userServerClient;
    @Autowired
    private RedisUtils redisUtils;

    /**
     * 发布动态，采用Feed流读扩散形式(拉流模式)，将发布成功的id添加到所有好友的时间轴索引中
     * 详情可见 https://zhuanlan.zhihu.com/p/400694641
     * @param momentsForm 动态内容
     * @param files 图片/视频等文件
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MomentsVo addMoments(MomentsForm momentsForm, MultipartFile[] files) {
        if (Objects.isNull(momentsForm)) {
            throw new CommonException(CommonExceptionEnum.INVALID_PARAMS);
        }
        // 获取登录信息
        UserVo userVo = loginInfoUtil.userInfo();
        // 开始创建动态
        Date current = new Date();
        Moments moments = new Moments(momentsForm);
        // todo 目前只有图文消息类型
        moments.setType(0);
        // 设置是否有额外数据
        moments.setHasExtra(files.length > 0);
        // 内容处理/过滤
        moments.setContent(ContentUtils.processContent(moments.getContent()));
        moments.setUserId(userVo.getAccountId());
        moments.setCreateTime(current);
        moments.setUpdateTime(current);
        moments.setPublishIp(IPUtils.getIPAddress(getRequest()));
        // 上传媒体文件
        int index = 0;
        List<MomentExtraMedia> mediaList = new ArrayList<>();
        // 获取日期
        String dateStr = TimeUtil.getTimeStr("yyyyMM");
        for (MultipartFile file : files) {
            try {
                String path;
                String originFileName = file.getOriginalFilename();
                if (FileUtils.isImage(originFileName)) {
                    // 如果是图片
                    path = FileUtils.fileUpload(file, MomentsBaseConstant.MOMENTS_IMAGE_DIR + dateStr,
                            FileNameGenerateStrategy.UUID_NAME);
                } else if (FileUtils.isVideo(originFileName)) {
                    // 如果是视频
                    path = FileUtils.fileUpload(file, MomentsBaseConstant.MOMENTS_VIDEO_DIR + dateStr,
                            FileNameGenerateStrategy.UUID_NAME);
                } else {
                    LOGGER.info("文件类型不支持[{}]", originFileName);
                    throw new CommonException(CommonExceptionEnum.FILE_TYPE_NOT_SUPPORT);
                }
                index ++;
                MomentExtraMedia media = new MomentExtraMedia();
                media.setMediaPath(path);
                media.setSortIndex(index);
                media.setCreateTime(current);
                media.setUserId(userVo.getAccountId());
                mediaList.add(media);
            } catch (IOException e) {
                LOGGER.error("文件上传失败", e);
                // 删除已经上传的文件
                FileUtils.deleteFiles(mediaList.stream().map(MomentExtraMedia::getMediaPath).collect(Collectors.toList()));
                throw new CommonException(CommonExceptionEnum.FILE_UPLOAD_FAIL);
            }
        }
        // 生成分布式id
        Long id = generateMomentId();
        // 设置动态的id
        moments.setId(id);
        // 给媒体信息设置动态id
        mediaList.forEach(e -> e.setMomentId(id));
        boolean result = this.save(moments);
        if (!result) {
            LOGGER.info("创建动态失败，删除上传文件");
            // 删除已经上传的文件
            FileUtils.deleteFiles(mediaList.stream().map(MomentExtraMedia::getMediaPath).collect(Collectors.toList()));
            throw new CommonException(CommonExceptionEnum.CREATE_MOMENTS_ERROR);
        } else {
            // 保存额外信息
            if (!CollectionUtils.isEmpty(mediaList)) {
                for (MomentExtraMedia extraMedia : mediaList) {
                    extraMediaService.save(extraMedia);
                }
            }
        }
        MomentsVo momentsVo = new MomentsVo(moments);
        momentsVo.setMediaInfos(mediaList.stream().map(MomentExtraMediaVo::new).collect(Collectors.toList()));
        // 主动推送动态至所有有权可见的人
        int pushCount = pushOrRemoveMoment2Friends(userVo.getAccountId(), id, true);
        LOGGER.info("推送动态至[{}]个好友", pushCount);

        return momentsVo;
    }

    @Override
    public MomentsVo getById(long id) {
        return null;
    }

    /**
     * 根据id批量获取动态信息
     * @param ids
     * @return
     */
    private List<MomentsVo> getInfoByIds(List<Long> ids) {
        Collection<Moments> moments = this.listByIds(ids);
        List<MomentsVo> momentsVoList = new ArrayList<>();
        for (Moments moment : moments) {
            MomentsVo vo = new MomentsVo(moment);
            // 查询额外信息
            List<MomentExtraMedia> extraMedia = extraMediaService.list(new QueryWrapper<MomentExtraMedia>()
                    .eq("moment_id", moment.getId())
                    .orderByAsc("sort_index"));
            if (!CollectionUtils.isEmpty(extraMedia)) {
                vo.setMediaInfos(extraMedia.stream().map(MomentExtraMediaVo::new).collect(Collectors.toList()));
            }
            momentsVoList.add(vo);
        }
        return momentsVoList;
    }

    @Override
    public List<MomentsVo> listMoments(Page<MomentsVo> page) {
        // 获取登录信息
        UserVo userVo = loginInfoUtil.userInfo();
        String key = RedisKeyConstant.MOMENTS_FEED_STREAM_TIMELINE_KEY + userVo.getAccountId();
        Long total = redisUtils.countZSet(key);
        page.setTotal(total);
        if (total <= 0)  {
            return page.getRecords();
        }
        // 处理分页信息
        long pageSize = page.getSize();
        long current = page.getCurrent();
        long jumpSize = pageSize * (current - 1);
        Set<Object> idSet = redisUtils.rangeByScore(key, jumpSize, pageSize);
        if (CollectionUtils.isEmpty(idSet)) {
            return page.getRecords();
        }
        // 设置详情
        List<Long> idList = idSet.stream().map(e -> Long.valueOf(e.toString())).collect(Collectors.toList());
        List<MomentsVo> result = getInfoByIds(idList);
        page.setRecords(result);
        return result;
    }

    /**
     * 推送自己的动态至所有有权可见的好友
     * 或者从好友时间轴中删除动态
     * @param userId
     * @return
     */
    public int pushOrRemoveMoment2Friends(long userId, long momentId, boolean push) {
        // 先获取所有有权可见动态的好友列表
        Result<List<Long>> requestResult = userServerClient.listShouldShowMomentFriends(userId);
        if (requestResult == null || !requestResult.isSuccess() || CollectionUtils.isEmpty(requestResult.getData())) {
            LOGGER.info("获取列表为空");
            return 0;
        }
        List<Long> list = requestResult.getData();
        // todo 多线程批量执行
        for (Long friendId : list) {
            if (push) {
                pushMoment(friendId, momentId);
            } else {
                removeMoment(friendId, momentId);
            }
        }
        return list.size();
    }

    /**
     * 推送自己的所有动态至指定好友
     * 或者从好友时间轴中删除所有动态
     * @param fromId 来源id
     * @param toId 目标id
     * @param push
     * @return
     */
    public int removeOrPushAllMomentsFromFriends(long fromId, long toId, boolean push) {
        // 判断是否有权限拉取
        if (push) {
            Result<Boolean> canVisitResult = userServerClient.canVisitMoments(toId, fromId);
            if (!canVisitResult.isSuccess() || !canVisitResult.getData()) {
                LOGGER.info("无权拉取好友动态");
                return 0;
            }
        }
        // 先获取自己的所有动态
        List<Moments> list = this.list(new QueryWrapper<Moments>().eq("user_id", fromId).eq("is_delete", 0));
        int count = 0;
        for (Moments moment : list) {
            long momentId = moment.getId();
            if (PublishScopeEnum.ALL.equal(moment.getPublishScope())) {
                pushOrRemoveMoment2Friend(toId, momentId, push);
                count ++;
            } else {
                // todo 根据不同的发布范围判断是否有权可见
                count ++;
            }
        }
        return count;
    }

    /**
     * 推送自己的动态至所指定的好友
     * 或者从指定好友时间轴中删除动态
     * @return
     */
    private void pushOrRemoveMoment2Friend(long friendId, long momentId, boolean push) {
        if (push) {
            pushMoment(friendId, momentId);
        } else {
            removeMoment(friendId, momentId);
        }
    }


    /**
     * 将动态插入到对应的时间轴中
     * @param friendId
     * @param momentId
     */
    private void pushMoment(long friendId, long momentId) {
        redisUtils.zAdd(RedisKeyConstant.MOMENTS_FEED_STREAM_TIMELINE_KEY + friendId, momentId, momentId);
    }

    /**
     * 从对应的时间轴中移除动态
     * @param friendId
     * @param momentId
     */
    private void removeMoment(long friendId, long momentId) {
        redisUtils.zRemove(RedisKeyConstant.MOMENTS_FEED_STREAM_TIMELINE_KEY + friendId, momentId);
    }

    /**
     * 生成分布式Id
     * @return
     */
    private Long generateMomentId() {
        // 生成账号Id
        Result<Long> accountIdResponse = idGeneratorClient.nextId(MomentsBaseConstant.COMMENTS_ID_BIZ_TYPE, MomentsBaseConstant.COMMENTS_ID_TOKEN);
        if (accountIdResponse.getCode() != HttpStatus.SC_OK || Objects.isNull(accountIdResponse.getData())) {
            LOGGER.info("Id生成错误:{}", accountIdResponse.getMessage());
            throw new CommonException(CommonExceptionEnum.ERROR_GENERATOR_ACCOUNT_ID);
        }
        return accountIdResponse.getData();
    }

    private HttpServletRequest getRequest() {
        return  ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}
