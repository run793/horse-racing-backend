package com.horseracing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.horseracing.dto.WxLoginDTO;
import com.horseracing.entity.User;
import com.horseracing.entity.UserYard;
import com.horseracing.mapper.UserMapper;
import com.horseracing.mapper.UserYardMapper;
import com.horseracing.service.UserService;
import com.horseracing.service.WxMiniService;
import com.horseracing.vo.UserInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 用户服务实现
 *
 * @author rzf
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private WxMiniService wxMiniService;

    @Autowired
    private UserYardMapper userYardMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfoVO wxLogin(WxLoginDTO dto) {
        // 通过code获取openid
        String openid = wxMiniService.getOpenidByCode(dto.getCode());
        if (openid == null) {
            return null;
        }

        // 查询用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        User user = this.getOne(wrapper);

        if (user == null) {
            // 新用户注册
            user = new User();
            user.setOpenid(openid);
            user.setNickname(dto.getNickname() != null ? dto.getNickname() : "游客");
            user.setAvatarUrl(dto.getAvatarUrl());
            user.setCurrentBait(100); // 初始饵料
            user.setUnlockedSeaArea(1); // 初始解锁第一海域
            user.setMaxLevel(1);
            user.setOfflineIncomeTime(LocalDateTime.now());
            user.setLastStealTime(null);
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            this.save(user);

            // 初始解锁第一个庭院槽位
            UserYard yard = new UserYard();
            yard.setUserId(user.getId());
            yard.setSlotId(1);
            yard.setIsUnlocked(1);
            yard.setCreateTime(LocalDateTime.now());
            yard.setUpdateTime(LocalDateTime.now());
            userYardMapper.insert(yard);
        } else {
            // 更新用户信息
            if (dto.getNickname() != null) {
                user.setNickname(dto.getNickname());
            }
            if (dto.getAvatarUrl() != null) {
                user.setAvatarUrl(dto.getAvatarUrl());
            }
            user.setUpdateTime(LocalDateTime.now());
            this.updateById(user);
        }

        return convertToVO(user);
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            return null;
        }
        return convertToVO(user);
    }

    @Override
    public Integer collectOfflineIncome(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            return 0;
        }

        // 计算离线收益：根据最大等级，每小时产生一定饵料
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(user.getOfflineIncomeTime(), now);
        long hours = duration.toHours();

        // 每小时收益 = 最大等级 * 2
        int income = (int) (user.getMaxLevel() * 2 * hours);
        if (income <= 0) {
            return 0;
        }

        // 更新离线收益计算时间
        user.setCurrentBait(user.getCurrentBait() + income);
        user.setOfflineIncomeTime(now);
        user.setUpdateTime(now);
        this.updateById(user);

        return income;
    }

    /**
     * 转换为VO
     */
    private UserInfoVO convertToVO(User user) {
        UserInfoVO vo = new UserInfoVO();
        vo.setUserId(user.getId());
        vo.setOpenid(user.getOpenid());
        vo.setNickname(user.getNickname());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setCurrentBait(user.getCurrentBait());
        vo.setUnlockedSeaArea(user.getUnlockedSeaArea());
        vo.setMaxLevel(user.getMaxLevel());
        vo.setLastStealTime(user.getLastStealTime());

        // 判断今天是否已经蹭过饵料
        LocalDateTime lastSteal = user.getLastStealTime();
        if (lastSteal == null) {
            vo.setCanStealToday(true);
        } else {
            LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            vo.setCanStealToday(lastSteal.isBefore(today));
        }

        // 计算离线收益（展示用）
        Duration duration = Duration.between(user.getOfflineIncomeTime(), LocalDateTime.now());
        vo.setOfflineIncome(duration.toHours() * user.getMaxLevel() * 2);

        // 统计已解锁槽位数
        LambdaQueryWrapper<UserYard> yardWrapper = new LambdaQueryWrapper<>();
        yardWrapper.eq(UserYard::getUserId, user.getId());
        yardWrapper.eq(UserYard::getIsUnlocked, 1);
        long count = userYardMapper.selectCount(yardWrapper);
        vo.setUnlockedSlots((int) count);

        return vo;
    }
}
