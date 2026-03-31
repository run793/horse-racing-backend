package com.horseracing.controller;

import com.horseracing.common.Result;
import com.horseracing.dto.WxLoginDTO;
import com.horseracing.service.UserService;
import com.horseracing.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * @author rzf
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户接口", description = "用户登录、信息查询、收益领取")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 微信登录
     */
    @PostMapping("/wx-login")
    @Operation(summary = "微信小程序登录", description = "通过微信code登录，返回用户信息")
    public Result<UserInfoVO> wxLogin(@Validated @RequestBody WxLoginDTO dto) {
        UserInfoVO vo = userService.wxLogin(dto);
        if (vo == null) {
            return Result.error(400, "微信登录失败，请重试");
        }
        return Result.success(vo);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/info/{userId}")
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户详细信息")
    public Result<UserInfoVO> getUserInfo(@PathVariable Long userId) {
        UserInfoVO vo = userService.getUserInfo(userId);
        if (vo == null) {
            return Result.error(404, "用户不存在");
        }
        return Result.success(vo);
    }

    /**
     * 领取离线收益
     */
    @PostMapping("/collect-offline-income/{userId}")
    @Operation(summary = "领取离线收益", description = "用户领取挂机获得的金币收益")
    public Result<Integer> collectOfflineIncome(@PathVariable Long userId) {
        Integer income = userService.collectOfflineIncome(userId);
        return Result.success(income);
    }
}
