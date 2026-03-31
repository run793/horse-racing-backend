package com.horseracing.vo;

import lombok.Data;

/**
 * 蹭饵料结果VO
 *
 * @author rzf
 */
@Data
public class StealResultVO {

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 获得饵料数量
     */
    private Integer baitAmount;

    /**
     * 剩余饵料
     */
    private Integer currentBait;

    /**
     * 失败消息
     */
    private String message;
}
