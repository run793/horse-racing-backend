package com.horseracing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户生物库存实体（用户拥有的素材）
 *
 * @author rzf
 */
@Data
@TableName("user_creature")
public class UserCreature {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 生物ID
     */
    private Long creatureId;

    /**
     * 拥有数量
     */
    private Integer count;

    /**
     * 是否已点亮图鉴 0-否 1-是
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
