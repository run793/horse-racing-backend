package com.horseracing.controller;

import com.horseracing.common.Result;
import com.horseracing.service.SocialService;
import com.horseracing.vo.StealResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 社交控制器
 *
 * @author rzf
 */
@RestController
@RequestMapping("/social")
@Tag(name = "社交接口", description = "蹭饵料、分享奖励等社交玩法")
public class SocialController {

    @Autowired
    private SocialService socialService;

    /**
     * 蹭好友饵料
     */
    @PostMapping("/steal/{userId}/{friendId}")
    @Operation(summary = "蹭好友饵料", description = "从好友那里偷一部分饵料，每天一次")
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
    @Operation(summary = "检查是否可蹭", description = "检查用户今天是否已经蹭过饵料")
    public Result<Boolean> canStealToday(@PathVariable Long userId) {
        return Result.success(socialService.canStealToday(userId));
    }

    /**
     * 分享奖励
     */
    @PostMapping("/share-reward/{userId}")
    @Operation(summary = "分享获得奖励", description = "分享游戏后获得额外饵料奖励")
    public Result<Integer> shareReward(@PathVariable Long userId) {
        return Result.success(socialService.shareReward(userId));
    }
}
