-- 《海错图》微信小游戏 数据库初始化脚本

-- 创建数据库
CREATE DATABASE IF NOT EXISTS haicuotu DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE haicuotu;

-- 1. 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `openid` VARCHAR(100) NOT NULL COMMENT '微信openid',
    `nickname` VARCHAR(100) DEFAULT NULL COMMENT '微信昵称',
    `avatar_url` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `current_bait` INT NOT NULL DEFAULT 0 COMMENT '当前饵料数量',
    `unlocked_sea_area` INT NOT NULL DEFAULT 1 COMMENT '已解锁海域ID',
    `max_level` INT NOT NULL DEFAULT 1 COMMENT '最大合成等级',
    `offline_income_time` DATETIME NOT NULL COMMENT '离线收益计算时间',
    `last_steal_time` DATETIME DEFAULT NULL COMMENT '最后蹭饵料时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_openid` (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 海洋生物配置表
CREATE TABLE IF NOT EXISTS `creature` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '生物名称',
    `level` INT NOT NULL COMMENT '生物等级 1-30',
    `sea_area` INT NOT NULL COMMENT '所属海域 1-3',
    `combine_from` VARCHAR(50) DEFAULT NULL COMMENT '合成需要的两个低级生物ID，逗号分隔',
    `image_url` VARCHAR(500) DEFAULT NULL COMMENT '图片资源路径',
    `description` TEXT DEFAULT NULL COMMENT '科普文案',
    `is_mythical` TINYINT NOT NULL DEFAULT 0 COMMENT '是否神兽 0-否 1-是',
    `combine_cost` INT NOT NULL DEFAULT 0 COMMENT '合成所需饵料',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    PRIMARY KEY (`id`),
    KEY `idx_level` (`level`),
    KEY `idx_sea_area` (`sea_area`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='海洋生物配置表';

-- 3. 用户生物库存表
CREATE TABLE IF NOT EXISTS `user_creature` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `creature_id` BIGINT NOT NULL COMMENT '生物ID',
    `count` INT NOT NULL DEFAULT 0 COMMENT '拥有数量',
    `is_unlocked` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已点亮图鉴 0-否 1-是',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_creature` (`user_id`, `creature_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户生物库存表';

-- 4. 用户庭院表
CREATE TABLE IF NOT EXISTS `user_yard` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `slot_id` INT NOT NULL COMMENT '槽位ID 1-8',
    `creature_id` BIGINT DEFAULT NULL COMMENT '放置的生物ID',
    `is_unlocked` TINYINT NOT NULL DEFAULT 0 COMMENT '解锁状态 0-未解锁 1-已解锁',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_slot` (`user_id`, `slot_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户庭院表';

-- 5. 用户图鉴表
CREATE TABLE IF NOT EXISTS `user_illustration` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `creature_id` BIGINT NOT NULL COMMENT '生物ID',
    `unlock_time` DATETIME NOT NULL COMMENT '点亮时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除 0-否 1-是',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_creature` (`user_id`, `creature_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户图鉴点亮记录表';

-- 初始化数据：30级合成路径，从磷虾开始
-- 这里只插入前几级，后续可以补充完整
INSERT INTO `creature` (`name`, `level`, `sea_area`, `combine_from`, `description`, `is_mythical`, `combine_cost`) VALUES
('磷虾', 1, 1, NULL, '《海错图》记载："虾生海中，小者如指，大者盈尺。群游水面，随波逐流。" 最基础的海洋生物，合成之路从这里开始。', 0, 0),
('小虾', 2, 1, '1,1', '小型虾类，活跃在浅海区域，是合成进阶的基础。', 0, 2),
('对虾', 3, 1, '2,2', '常见的食用虾类，体型较大，肉质鲜美。', 0, 5),
('龙虾', 4, 1, '3,3', '海中珍品，身披坚甲，双钳威武，是浅海海域的顶级猎手。', 0, 10),
('帝王蟹', 5, 1, '4,4', '深海巨无霸，体型巨大，肉质饱满，是浅海海域的终极生物。', 0, 20);

-- 说明：完整30级合成数据需要后续补充，这里只展示结构
