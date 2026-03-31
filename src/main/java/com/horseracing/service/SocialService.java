package com.horseracing.service;

import com.horseracing.vo.StealResultVO;

/**
 * 社交服务接口
 *
 * @author rzf
 */
public interface SocialService {

    /**
     * 蹭好友饵料
     *
     * @param userId 当前用户ID
     * @param friendId 好友用户ID
     * @return 蹭到的饵料数量
     */
    StealResultVO stealBait(Long userId, Long friendId);

    /**
     * 检查今天是否已经蹭过
     *
     * @param userId 用户ID
     * @return 是否可以蹭
     */
    Boolean canStealToday(Long userId);

    /**
     * 分享奖励
     *
     * @param userId 用户ID
     * @return 奖励饵料数量
     */
    Integer shareReward(Long userId);
}
