package com.horseracing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 海洋生物配置实体
 *
 * @author rzf
 */
@Data
@TableName("creature")
public class Creature {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 生物名称
     */
    private String name;

    /**
     * 生物等级 1-30
     */
    private Integer level;

    /**
     * 所属海域 1-3
     */
    private Integer seaArea;

    /**
     * 合成需要的两个低级生物ID
     */
    private String combineFrom;

    /**
     * 图片资源路径
     */
    private String imageUrl;

    /**
     * 科普文案
     */
    private String description;

    /**
     * 是否神兽 0-否 1-是
     */
    private Integer isMythical;

    /**
     * 合成所需饵料
     */
    private Integer combineCost;

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
