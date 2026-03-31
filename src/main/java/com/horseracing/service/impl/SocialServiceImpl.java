package com.horseracing.service.impl;

import com.horseracing.entity.User;
import com.horseracing.mapper.UserMapper;
import com.horseracing.service.SocialService;
import com.horseracing.vo.StealResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * 社交服务实现
 *
 * @author rzf
 */
@Service
public class SocialServiceImpl implements SocialService {

    @Autowired
    private UserMapper userMapper;

    private final Random random = new Random();

    @Override
    public StealResultVO stealBait(Long userId, Long friendId) {
        StealResultVO result = new StealResultVO();
        result.setSuccess(false);

        // 检查今天是否已经蹭过
        User user = userMapper.selectById(userId);
        if (user == null) {
            result.setMessage("用户不存在");
            return result;
        }

        if (!canStealToday(userId)) {
            result.setMessage("今天已经蹭过饵料了，请明天再来");
            return result;
        }

        // 检查好友是否存在
        User friend = userMapper.selectById(friendId);
        if (friend == null) {
            result.setMessage("好友不存在");
            return result;
        }

        // 随机蹭饵料：10 ~ 最大等级 * 2
        int maxBait = friend.getMaxLevel() * 2 + 10;
        int baitAmount = random.nextInt(maxBait) + 10;

        // 增加用户饵料
        user.setCurrentBait(user.getCurrentBait() + baitAmount);
        user.setLastStealTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);

        result.setSuccess(true);
        result.setBaitAmount(baitAmount);
        result.setCurrentBait(user.getCurrentBait());

        return result;
    }

    @Override
    public Boolean canStealToday(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null || user.getLastStealTime() == null) {
            return true;
        }

        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        return user.getLastStealTime().isBefore(today);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer shareReward(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return 0;
        }

        // 分享奖励固定 50 饵料
        int reward = 50;
        user.setCurrentBait(user.getCurrentBait() + reward);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);

        return reward;
    }
}
