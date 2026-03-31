package com.horseracing.controller;

import com.horseracing.common.Result;
import com.horseracing.dto.YardPlaceDTO;
import com.horseracing.service.YardService;
import com.horseracing.vo.YardSlotVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 庭院控制器
 *
 * @author rzf
 */
@RestController
@RequestMapping("/yard")
public class YardController {

    @Autowired
    private YardService yardService;

    /**
     * 获取用户庭院信息
     */
    @GetMapping("/{userId}")
    public Result<List<YardSlotVO>> getUserYard(@PathVariable Long userId) {
        return Result.success(yardService.getUserYard(userId));
    }

    /**
     * 放置/更换神兽
     */
    @PostMapping("/place")
    public Result<Boolean> placeCreature(@Validated @RequestBody YardPlaceDTO dto) {
        Boolean result = yardService.placeCreature(dto);
        if (!result) {
            return Result.error(400, "放置失败，槽位未解锁");
        }
        return Result.success(true);
    }

    /**
     * 解锁槽位
     */
    @PostMapping("/unlock/{userId}/{slotId}/{cost}")
    public Result<Boolean> unlockSlot(@PathVariable Long userId, @PathVariable Integer slotId, @PathVariable Integer cost) {
        Boolean result = yardService.unlockSlot(userId, slotId, cost);
        if (!result) {
            return Result.error(400, "解锁失败，饵料不足");
        }
        return Result.success(true);
    }
}
