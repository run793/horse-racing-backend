package com.horseracing.controller;

import com.horseracing.common.Result;
import com.horseracing.service.FishService;
import com.horseracing.vo.FishResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 每日钓鱼奇遇控制器
 *
 * @author rzf
 */
@RestController
@RequestMapping("/fish")
public class FishController {

    @Autowired
    private FishService fishService;

    /**
     * 钓鱼
     */
    @PostMapping("/do/{userId}")
    public Result<FishResultVO> doFish(@PathVariable Long userId) {
        FishResultVO result = fishService.doFish(userId);
        if (!result.getSuccess()) {
            return Result.error(400, result.getMessage());
        }
        return Result.success(result);
    }

    /**
     * 获取今日剩余钓鱼次数
     */
    @GetMapping("/remaining/{userId}")
    public Result<Integer> getRemainingTimes(@PathVariable Long userId) {
        return Result.success(fishService.getRemainingTimes(userId));
    }

    /**
     * 观看广告增加钓鱼次数
     */
    @PostMapping("/add-by-ad/{userId}")
    public Result<Integer> addFishTimesByAd(@PathVariable Long userId) {
        return Result.success(fishService.addFishTimesByAd(userId));
    }
}
