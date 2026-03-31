package com.horseracing.controller;

import com.horseracing.common.Result;
import com.horseracing.service.IllustrationService;
import com.horseracing.vo.CreatureInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 图鉴控制器
 *
 * @author rzf
 */
@RestController
@RequestMapping("/illustration")
@Tag(name = "图鉴接口", description = "用户图鉴查询、统计信息")
public class IllustrationController {

    @Autowired
    private IllustrationService illustrationService;

    /**
     * 获取用户已点亮图鉴列表
     */
    @GetMapping("/list/{userId}")
    @Operation(summary = "获取用户图鉴列表", description = "查询用户已经解锁的所有生物图鉴")
    public Result<List<CreatureInfoVO>> getUserIllustrationList(@PathVariable Long userId) {
        return Result.success(illustrationService.getUserIllustrationList(userId));
    }

    /**
     * 获取用户图鉴统计
     */
    @GetMapping("/stats/{userId}")
    @Operation(summary = "获取图鉴统计", description = "返回[已解锁数量, 总数量]")
    public Result<int[]> getUserIllustrationStats(@PathVariable Long userId) {
        return Result.success(illustrationService.getUserIllustrationStats(userId));
    }
}
