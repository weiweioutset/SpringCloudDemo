-- 新建用户信息表
CREATE TABLE `user_info_001`  (
                                  `account_id` bigint(20) NOT NULL COMMENT '账户Id',
                                  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
                                  `english_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '英文名',
                                  `gender` tinyint(1) NOT NULL DEFAULT 1 COMMENT '性别 1男 0女',
                                  `register_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账号注册时间',
                                  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '账号更新时间',
                                  `phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
                                  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
                                  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '加密密码',
                                  `birthday` datetime NULL DEFAULT NULL COMMENT '生日',
                                  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '0 申请中 1有效 2删除 3封号',
                                  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像地址',
                                  `thumbnail_avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像缩略图地址',
                                  PRIMARY KEY (`account_id`) USING BTREE,
                                  INDEX `idx_phone`(`phone` ASC) USING BTREE INVISIBLE COMMENT '手机号索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;


-- 新建角色表
CREATE TABLE `role_info` (
                             `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
                             `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色代码',
                             `role_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `create_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                             `update_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '更新人',
                             PRIMARY KEY (`id`, `role_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- 新建用户角色关系表
CREATE TABLE `user_role_relation` (
                                      `user_id` bigint(20) NOT NULL COMMENT '用户id',
                                      `role_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色id',
                                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `create_user` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '创建人',
                                      `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间',
                                      PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
                                      INDEX `idx_user_id`(`user_id` ASC) USING BTREE COMMENT '用户id索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关系表' ROW_FORMAT = Dynamic;

-- 新建用户登录日志表
CREATE TABLE `user_login_log`  (
                                       `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                       `account_id` bigint(20) NOT NULL COMMENT '账号Id',
                                       `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
                                       `login_time` datetime NOT NULL COMMENT '登录时间',
                                       `login_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录ip',
                                       `client_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备类型 Android/IOS/PC',
                                       `client_os` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备系统',
                                       `login_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '登录方式 0密码登录 1扫码登录',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       INDEX `idx_account`(`account_id` ASC) USING BTREE COMMENT '账号id索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户登录日志表' ROW_FORMAT = Dynamic;

-- 新增菜单表
CREATE TABLE `menu_info`  (
                                       `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                                       `menu_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单代码',
                                       `menu_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名',
                                       `ant_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单地址',
                                       `parent_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父级菜单',
                                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       `create_user` int(11) NOT NULL COMMENT '创建人',
                                       `update_user` int(11) NOT NULL COMMENT '更新人',
                                       PRIMARY KEY (`id`, `menu_code`) USING BTREE,
                                       INDEX `idx_parent_menu`(`parent_code` ASC) USING BTREE INVISIBLE COMMENT '父级菜单索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- 新增菜单与角色关系表
CREATE TABLE `role_menu_relation`  (
                                       `role_id` int(64) NOT NULL COMMENT '角色id',
                                       `menu_id` int(64) NOT NULL COMMENT '菜单id',
                                       `create_user` int(64) NOT NULL COMMENT '创建人',
                                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单与角色关系表' ROW_FORMAT = Dynamic;

-- 新增朋友关系表
CREATE TABLE `t_friends`  (
                                       `user_id` bigint(20) NOT NULL COMMENT '用户id',
                                       `friend_id` bigint(20) NOT NULL COMMENT '朋友id',
                                       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
                                       `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                       `chats_only` tinyint(1) NOT NULL DEFAULT 0 COMMENT '仅聊天',
                                       `hide_my_posts` tinyint(1) NOT NULL DEFAULT 0 COMMENT '不让他看我动态',
                                       `hide_his_posts` tinyint(1) NOT NULL DEFAULT 0 COMMENT '不看他动态',
                                       `update_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       `from_source` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '添加来源',
                                       `focus` tinyint(1) NOT NULL DEFAULT 0 COMMENT '星标',
                                       `block` tinyint(1) NOT NULL DEFAULT 0 COMMENT '黑名单',
                                       `apply_status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '申请状态 0申请中 1同意 2拒绝',
                                       PRIMARY KEY (`user_id`, `friend_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '朋友关系表' ROW_FORMAT = Dynamic;
