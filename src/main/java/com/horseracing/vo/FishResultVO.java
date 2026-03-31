package com.horseracing.vo;

import lombok.Data;

/**
 * 钓鱼结果VO
 *
 * @author rzf
 */
@Data
public class FishResultVO {

    /**
     * 是否成功钓鱼
     */
    private Boolean success;

    /**
     * 奖励类型：bait=饵料, creature=生物碎片, rare=稀有生物
     */
    private String rewardType;

    /**
     * 奖励饵料数量
     */
    private Integer baitAmount;

    /**
     * 奖励生物ID（如果是生物奖励）
     */
    private Long creatureId;

    /**
     * 奖励生物名称
     */
    private String creatureName;

    /**
     * 是否新解锁
     */
    private Boolean isNewUnlock;

    /**
     * 剩余钓鱼次数
     */
    private Integer remainingTimes;

    /**
     * 消息
     */
    private String message;
}
