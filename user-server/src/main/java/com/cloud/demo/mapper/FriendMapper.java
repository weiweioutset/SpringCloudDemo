package com.cloud.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.demo.po.Friend;
import com.cloud.demo.vo.FriendVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author weiwei
 * @Date 2022/7/23 上午11:56
 * @Version 1.0
 * @Desc
 */
@Repository
@Mapper
public interface FriendMapper extends BaseMapper<Friend> {

    /**
     * 更新好友申请状态
     * @param friend
     * @return
     */
    int updateApplyStatus(Friend friend);

    /**
     * 查询好友列表
     * @param userId
     * @param block 0:正常好友 1:黑名单好友
     * @return
     */
    List<FriendVo> listFriends(@Param("userId") Long userId, @Param("block") int block);

    /**
     * 查询仅聊天好友列表
     * @param userId
     * @return
     */
    List<FriendVo> listChatsOnlyFriends(@Param("userId") Long userId);

    /**
     * 获取所有有权可见自己动态的好友列表
     * 去除黑名单/仅聊天/不让他看我/或者对方设置了相关权限的
     * @param userId
     * @return
     */
    List<Long> listShouldShowMomentFriends(@Param("userId") Long userId);

    /**
     * 是否有权访问对方动态
     * @param userId
     * @param friendId
     * @return
     */
    Integer canVisitMoments(@Param("userId") Long userId, @Param("friendId") Long friendId);
}
