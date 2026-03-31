package com.horseracing.controller;

import com.horseracing.common.Result;
import com.horseracing.service.SocialService;
import com.horseracing.vo.StealResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 社交控制器
 *
 * @author rzf
 */
@RestController
@RequestMapping("/social")
public class SocialController {

    @Autowired
    private SocialService socialService;

    /**
     * 蹭好友饵料
     */
    @PostMapping("/steal/{userId}/{friendId}")
    public Result<StealResultVO> stealBait(@PathVariable Long userId, @PathVariable Long friendId) {
        StealResultVO result = socialService.stealBait(userId, friendId);
        if (!result.getSuccess()) {
            return Result.error(400, result.getMessage());
        }
        return Result.success(result);
    }

    /**
     * 检查今天是否可以蹭饵料
     */
    @GetMapping("/can-steal/{userId}")
    public Result<Boolean> canStealToday(@PathVariable Long userId) {
        return Result.success(socialService.canStealToday(userId));
    }

    /**
     * 分享奖励
     */
    @PostMapping("/share-reward/{userId}")
    public Result<Integer> shareReward(@PathVariable Long userId) {
        return Result.success(socialService.shareReward(userId));
    }
}
