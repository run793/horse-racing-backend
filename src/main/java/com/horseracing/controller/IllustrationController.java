package com.horseracing.controller;

import com.horseracing.common.Result;
import com.horseracing.service.IllustrationService;
import com.horseracing.vo.CreatureInfoVO;
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
public class IllustrationController {

    @Autowired
    private IllustrationService illustrationService;

    /**
     * 获取用户已点亮图鉴列表
     */
    @GetMapping("/list/{userId}")
    public Result<List<CreatureInfoVO>> getUserIllustrationList(@PathVariable Long userId) {
        return Result.success(illustrationService.getUserIllustrationList(userId));
    }

    /**
     * 获取用户图鉴统计
     */
    @GetMapping("/stats/{userId}")
    public Result<int[]> getUserIllustrationStats(@PathVariable Long userId) {
        return Result.success(illustrationService.getUserIllustrationStats(userId));
    }
}
