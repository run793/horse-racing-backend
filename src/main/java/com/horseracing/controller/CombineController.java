package com.horseracing.controller;

import com.horseracing.common.Result;
import com.horseracing.dto.CombineDTO;
import com.horseracing.service.CombineService;
import com.horseracing.vo.CombineResultVO;
import com.horseracing.vo.CreatureInfoVO;
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
public class CombineController {

    @Autowired
    private CombineService combineService;

    /**
     * 合成操作
     */
    @PostMapping("/do")
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
    public Result<List<CreatureInfoVO>> getUserCreatureList(@PathVariable Long userId) {
        return Result.success(combineService.getUserCreatureList(userId));
    }

    /**
     * 获取全量生物配置列表
     */
    @GetMapping("/all-list")
    public Result<List<CreatureInfoVO>> getAllCreatureList() {
        return Result.success(combineService.getAllCreatureList());
    }

    /**
     * 解锁海域
     */
    @PostMapping("/unlock-sea-area/{userId}/{seaAreaId}")
    public Result<Boolean> unlockSeaArea(@PathVariable Long userId, @PathVariable Integer seaAreaId) {
        Boolean result = combineService.unlockSeaArea(userId, seaAreaId);
        if (!result) {
            return Result.error(400, "解锁失败，请先解锁前一个海域");
        }
        return Result.success(true);
    }
}
