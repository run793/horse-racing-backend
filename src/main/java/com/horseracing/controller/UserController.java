package com.horseracing.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.horseracing.common.Result;
import com.horseracing.entity.User;
import com.horseracing.service.UserService;
import com.horseracing.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户接口
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据ID获取用户详情
     */
    @ApiOperation("获取用户详情")
    @GetMapping("/{id}")
    public Result<UserInfoVO> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(convertToVO(user));
    }

    /**
     * 分页查询用户列表
     */
    @ApiOperation("分页查询用户列表")
    @GetMapping("/page")
    public Result<Page<UserInfoVO>> pageList(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size) {
        Page<User> page = userService.pageList(current, size);
        // 转换为 VO
        Page<UserInfoVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        List<UserInfoVO> voList = new ArrayList<>();
        for (User user : page.getRecords()) {
            voList.add(convertToVO(user));
        }
        voPage.setRecords(voList);
        return Result.success(voPage);
    }

    /**
     * 新增用户
     */
    @ApiOperation("新增用户")
    @PostMapping
    public Result<Long> add(@RequestBody User user) {
        userService.save(user);
        return Result.success(user.getId());
    }

    /**
     * 更新用户
     */
    @ApiOperation("更新用户信息")
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        boolean success = userService.updateById(user);
        if (success) {
            return Result.success();
        }
        return Result.error("更新失败");
    }

    /**
     * 删除用户
     */
    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean success = userService.removeById(id);
        if (success) {
            return Result.success();
        }
        return Result.error("删除失败");
    }

    /**
     * Entity 转换为 VO
     */
    private UserInfoVO convertToVO(User user) {
        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(user, vo);
        // 计算胜率
        if (user.getTotalGames() != null && user.getTotalGames() > 0 && user.getWinGames() != null) {
            vo.setWinRate((double) user.getWinGames() / user.getTotalGames() * 100);
        } else {
            vo.setWinRate(0.0);
        }
        return vo;
    }
}
