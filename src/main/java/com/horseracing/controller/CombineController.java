package com.horseracing.controller;

import com.horseracing.common.Result;
import com.horseracing.dto.CombineDTO;
import com.horseracing.service.CombineService;
import com.horseracing.vo.CombineResultVO;
import com.horseracing.vo.CreatureInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 合成控制器
 *
 * @author rzf
 */
@RestController
@RequestMapping("/combine")
@Tag(name = "合成接口", description = "生物合成、生物列表、海域解锁")
public class CombineController {

    @Autowired
    private CombineService combineService;

    /**
     * 合成操作
     */
    @PostMapping("/do")
    @Operation(summary = "合成生物", description = "用五个碎片合成一个完整生物")
    public Result<CombineResultVO> doCombine(@Validated @RequestBody CombineDTO dto) {
        CombineResultVO result = combineService.combine(dto);
        if (!result.getSuccess()) {
            return Result.error(400, result.getMessage());
        }
        return Result.success(result);
    }

    /**
     * 获取用户已解锁生物列表
     */
    @GetMapping("/user-list/{userId}")
    @Operation(summary = "获取用户已解锁生物", description = "查询当前用户拥有的所有生物信息")
    public Result<List<CreatureInfoVO>> getUserCreatureList(@PathVariable Long userId) {
        return Result.success(combineService.getUserCreatureList(userId));
    }

    /**
     * 获取全量生物配置列表
     */
    @GetMapping("/all-list")
    @Operation(summary = "获取全量生物列表", description = "获取游戏中所有生物的配置信息")
    public Result<List<CreatureInfoVO>> getAllCreatureList() {
        return Result.success(combineService.getAllCreatureList());
    }

    /**
     * 解锁海域
     */
    @PostMapping("/unlock-sea-area/{userId}/{seaAreaId}")
    @Operation(summary = "解锁新海域", description = "消耗金币解锁更高等级海域，可以获得更高级生物")
    public Result<Boolean> unlockSeaArea(@PathVariable Long userId, @PathVariable Integer seaAreaId) {
        Boolean result = combineService.unlockSeaArea(userId, seaAreaId);
        if (!result) {
            return Result.error(400, "解锁失败，请先解锁前一个海域");
        }
        return Result.success(true);
    }
}
