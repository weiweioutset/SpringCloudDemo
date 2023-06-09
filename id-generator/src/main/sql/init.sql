-- 创建token信息表
CREATE TABLE `tiny_id_token`  (
                                       `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
                                       `token` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT 'token',
                                       `biz_type` varchar(63) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '此token可访问的业务类型标识',
                                       `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '备注',
                                       `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'token信息表' ROW_FORMAT = Dynamic;

-- 创建ID信息表
CREATE TABLE `tiny_id_info`  (
                                       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                       `biz_type` varchar(63) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '业务类型，唯一',
                                       `begin_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '开始id，仅记录初始值，无其他含义。初始化时begin_id和max_id应相同',
                                       `max_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '当前最大id',
                                       `step` int(11) NULL DEFAULT 0 COMMENT '步长',
                                       `delta` int(11) NOT NULL DEFAULT 1 COMMENT '每次id增量',
                                       `remainder` int(11) NOT NULL DEFAULT 0 COMMENT '余数',
                                       `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
                                       `version` bigint(20) NOT NULL DEFAULT 0 COMMENT '版本号',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       UNIQUE INDEX `uniq_biz_type`(`biz_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'id信息表' ROW_FORMAT = Dynamic;