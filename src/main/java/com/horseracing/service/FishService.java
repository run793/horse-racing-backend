package com.horseracing.service;

import com.horseracing.vo.FishResultVO;

/**
 * 每日钓鱼奇遇服务接口
 *
 * @author rzf
 */
public interface FishService {

    /**
     * 钓鱼
     *
     * @param userId 用户ID
     * @return 钓鱼结果
     */
    FishResultVO doFish(Long userId);

    /**
     * 获取今日剩余钓鱼次数
     *
     * @param userId 用户ID
     * @return 剩余次数
     */
    int getRemainingTimes(Long userId);

    /**
     * 观看广告增加钓鱼次数
     *
     * @param userId 用户ID
     * @return 增加后的剩余次数
     */
    int addFishTimesByAd(Long userId);
}
