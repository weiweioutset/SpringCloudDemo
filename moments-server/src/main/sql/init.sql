-- 新建动态信息记录表
CREATE TABLE `t_moments`  (
                                       `id` bigint(20) NOT NULL COMMENT '主键',
                                       `content` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '动态内容',
                                       `user_id` bigint(20) NOT NULL COMMENT '动态作者',
                                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                       `type` tinyint(2) NOT NULL DEFAULT 0 COMMENT '动态类型 0图文',
                                       `has_extra` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否有额外内容(如图片视频等)',
                                       `extra_data` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '额外内容(保留字段)',
                                       `publish_scope` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '可见范围:All公开/Private私密/Include部分可见/Except部分不可见',
                                       `need_remind` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否需要提醒谁看',
                                       `is_delete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否被删除',
                                       `publish_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发布IP地址',
                                       `show_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '显示地址',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       INDEX `idx_user_id`(`user_id` ASC) USING BTREE INVISIBLE COMMENT '用户id索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户动态记录表' ROW_FORMAT = Dynamic;

-- 新建动态中图片/视频信息表
CREATE TABLE `moment_extra_media`  (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                       `moment_id` bigint(20) NOT NULL COMMENT '动态id，联合主键',
                                       `media_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片/视频地址',
                                       `thumbnail` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片缩略地址',
                                       `user_id` bigint(20) NOT NULL COMMENT '用户id',
                                       `index` int(2) NOT NULL DEFAULT 1 COMMENT '排序',
                                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       PRIMARY KEY (`id`, `moment_id`) USING BTREE,
                                       INDEX `idx_moment_id`(`moment_id` ASC) USING BTREE INVISIBLE COMMENT '动态id索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '动态中图片/视频信息表' ROW_FORMAT = Dynamic;