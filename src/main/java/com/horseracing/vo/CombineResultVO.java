package com.horseracing.vo;

import lombok.Data;

/**
 * 合成结果VO
 *
 * @author rzf
 */
@Data
public class CombineResultVO {

    /**
     * 是否合成成功
     */
    private Boolean success;

    /**
     * 合成得到的生物ID
     */
    private Long resultCreatureId;

    /**
     * 合成得到的生物名称
     */
    private String resultCreatureName;

    /**
     * 是否是新解锁
     */
    private Boolean isNewUnlock;

    /**
     * 是否升级了最大等级
     */
    private Boolean levelUpgraded;

    /**
     * 合成后剩余饵料
     */
    private Integer remainingBait;

    /**
     * 失败原因
     */
    private String message;
}
