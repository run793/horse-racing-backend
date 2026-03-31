package com.horseracing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户庭院实体（神兽放置）
 *
 * @author rzf
 */
@Data
@TableName("user_yard")
public class UserYard {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 槽位ID 1-8（初始1个，后续解锁）
     */
    private Integer slotId;

    /**
     * 放置的生物ID 为空表示未放置
     */
    private Long creatureId;

    /**
     * 解锁状态 0-未解锁 1-已解锁
     */
    private Integer isUnlocked;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDeleted;
}
