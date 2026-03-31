package com.horseracing.controller;

import com.horseracing.common.Result;
import com.horseracing.service.FishService;
import com.horseracing.vo.FishResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 每日钓鱼奇遇控制器
 *
 * @author rzf
 */
@RestController
@RequestMapping("/fish")
@Tag(name = "钓鱼接口", description = "每日钓鱼玩法，获得生物碎片、完整生物、金币奖励")
public class FishController {

    @Autowired
    private FishService fishService;

    /**
     * 钓鱼
     */
    @PostMapping("/do/{userId}")
    @Operation(summary = "开始钓鱼", description = "用户进行一次钓鱼，随机奖励饵料/普通生物/稀有生物")
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
    @Operation(summary = "获取剩余钓鱼次数", description = "查询用户今日还剩多少次钓鱼机会")
    public Result<Integer> getRemainingTimes(@PathVariable Long userId) {
        return Result.success(fishService.getRemainingTimes(userId));
    }

    /**
     * 观看广告增加钓鱼次数
     */
    @PostMapping("/add-by-ad/{userId}")
    @Operation(summary = "观看广告增加次数", description = "观看广告后增加3次钓鱼次数")
    public Result<Integer> addFishTimesByAd(@PathVariable Long userId) {
        return Result.success(fishService.addFishTimesByAd(userId));
    }
}
