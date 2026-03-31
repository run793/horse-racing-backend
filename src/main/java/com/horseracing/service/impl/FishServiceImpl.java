package com.horseracing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.horseracing.entity.*;
import com.horseracing.mapper.*;
import com.horseracing.service.FishService;
import com.horseracing.vo.FishResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * 钓鱼服务实现
 *
 * @author rzf
 */
@Service
public class FishServiceImpl implements FishService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CreatureMapper creatureMapper;

    @Autowired
    private UserCreatureMapper userCreatureMapper;

    @Autowired
    private UserIllustrationMapper userIllustrationMapper;

    private final Random random = new Random();

    // 初始免费钓鱼次数
    private static final int FREE_TIMES_PER_DAY = 3;

    @Override
    public FishResultVO doFish(Long userId) {
        FishResultVO result = new FishResultVO();
        result.setSuccess(false);

        User user = userMapper.selectById(userId);
        if (user == null) {
            result.setMessage("用户不存在");
            return result;
        }

        int remaining = getRemainingTimes(userId);
        if (remaining <= 0) {
            result.setMessage("今日钓鱼次数已用完，请明天再来或看广告增加次数");
            result.setRemainingTimes(0);
            return result;
        }

        // 随机抽奖
        double rand = random.nextDouble();
        if (rand < 0.5) {
            // 50% 概率获得饵料
            int baitAmount = random.nextInt(20) + 10;
            user.setCurrentBait(user.getCurrentBait() + baitAmount);
            user.setUpdateTime(LocalDateTime.now());
            userMapper.updateById(user);

            result.setSuccess(true);
            result.setRewardType("bait");
            result.setBaitAmount(baitAmount);
            result.setRemainingTimes(remaining - 1);
        } else if (rand < 0.85) {
            // 35% 概率获得普通生物碎片/完整生物
            // 从已解锁海域随机选一个生物
            int seaArea = user.getUnlockedSeaArea();
            LambdaQueryWrapper<Creature> wrapper = new LambdaQueryWrapper<>();
            wrapper.le(Creature::getSeaArea, seaArea);
            List<Creature> list = creatureMapper.selectList(wrapper);
            if (list.isEmpty()) {
                result.setRewardType("bait");
                int baitAmount = 15;
                user.setCurrentBait(user.getCurrentBait() + baitAmount);
                user.setUpdateTime(LocalDateTime.now());
                userMapper.updateById(user);
                result.setSuccess(true);
                result.setBaitAmount(baitAmount);
                result.setRemainingTimes(remaining - 1);
                return result;
            }

            Creature rewardCreature = list.get(random.nextInt(list.size()));
            giveCreatureToUser(userId, rewardCreature);

            // 检查是否新解锁
            LambdaQueryWrapper<UserIllustration> illusWrapper = new LambdaQueryWrapper<>();
            illusWrapper.eq(UserIllustration::getUserId, userId);
            illusWrapper.eq(UserIllustration::getCreatureId, rewardCreature.getId());
            UserIllustration illustration = userIllustrationMapper.selectOne(illusWrapper);

            result.setSuccess(true);
            result.setRewardType("creature");
            result.setCreatureId(rewardCreature.getId());
            result.setCreatureName(rewardCreature.getName());
            result.setIsNewUnlock(illustration == null);
            result.setRemainingTimes(remaining - 1);

            if (illustration == null) {
                // 添加图鉴记录
                UserIllustration illus = new UserIllustration();
                illus.setUserId(userId);
                illus.setCreatureId(rewardCreature.getId());
                illus.setUnlockTime(LocalDateTime.now());
                illus.setCreateTime(LocalDateTime.now());
                illus.setUpdateTime(LocalDateTime.now());
                userIllustrationMapper.insert(illus);
            }
        } else {
            // 15% 概率获得稀有生物（高等级）
            int seaArea = user.getUnlockedSeaArea();
            LambdaQueryWrapper<Creature> wrapper = new LambdaQueryWrapper<>();
            wrapper.le(Creature::getSeaArea, seaArea);
            wrapper.ge(Creature::getLevel, Math.max(10, user.getMaxLevel() - 5));
            List<Creature> list = creatureMapper.selectList(wrapper);

            Creature rewardCreature;
            if (list.isEmpty()) {
                wrapper = new LambdaQueryWrapper<>();
                wrapper.le(Creature::getSeaArea, seaArea);
                list = creatureMapper.selectList(wrapper);
                if (list.isEmpty()) {
                    result.setRewardType("bait");
                    int baitAmount = 30;
                    user.setCurrentBait(user.getCurrentBait() + baitAmount);
                    user.setUpdateTime(LocalDateTime.now());
                    userMapper.updateById(user);
                    result.setSuccess(true);
                    result.setBaitAmount(baitAmount);
                    result.setRemainingTimes(remaining - 1);
                    return result;
                }
                rewardCreature = list.get(random.nextInt(list.size()));
            } else {
                rewardCreature = list.get(random.nextInt(list.size()));
            }

            giveCreatureToUser(userId, rewardCreature);

            // 检查是否新解锁
            LambdaQueryWrapper<UserIllustration> illusWrapper = new LambdaQueryWrapper<>();
            illusWrapper.eq(UserIllustration::getUserId, userId);
            illusWrapper.eq(UserIllustration::getCreatureId, rewardCreature.getId());
            UserIllustration illustration = userIllustrationMapper.selectOne(illusWrapper);

            result.setSuccess(true);
            result.setRewardType("rare");
            result.setCreatureId(rewardCreature.getId());
            result.setCreatureName(rewardCreature.getName());
            result.setIsNewUnlock(illustration == null);
            result.setRemainingTimes(remaining - 1);

            if (illustration == null) {
                UserIllustration illus = new UserIllustration();
                illus.setUserId(userId);
                illus.setCreatureId(rewardCreature.getId());
                illus.setUnlockTime(LocalDateTime.now());
                illus.setCreateTime(LocalDateTime.now());
                illus.setUpdateTime(LocalDateTime.now());
                userIllustrationMapper.insert(illus);
            }
        }

        return result;
    }

    @Override
    public int getRemainingTimes(Long userId) {
        // 简化实现：基于用户最后钓鱼时间判断
        // 这里简化处理，每天免费3次，记录方式可以优化到单独表
        // 当前简化：免费次数 = 3 - 今日已钓次数（通过估算）
        // 实际项目中应该单独建 daily_fish 表记录

        // 简化逻辑：每天固定3次，广告可以加3次
        // 这里返回固定3次，实际需要存储
        // 为了简化，我们直接返回 3 次免费 + 0 次广告
        // 如果要精准，需要额外建表，MVP版本先简化
        return 3;
    }

    @Override
    public int addFishTimesByAd(Long userId) {
        // 观看广告增加 3 次钓鱼次数
        // 简化实现，实际需要存储
        return 3;
    }

    /**
     * 给用户奖励生物
     */
    private void giveCreatureToUser(Long userId, Creature creature) {
        LambdaQueryWrapper<UserCreature> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCreature::getUserId, userId);
        wrapper.eq(UserCreature::getCreatureId, creature.getId());
        UserCreature userCreature = userCreatureMapper.selectOne(wrapper);

        if (userCreature == null) {
            userCreature = new UserCreature();
            userCreature.setUserId(userId);
            userCreature.setCreatureId(creature.getId());
            userCreature.setCount(1);
            userCreature.setIsUnlocked(0);
            userCreature.setCreateTime(LocalDateTime.now());
            userCreature.setUpdateTime(LocalDateTime.now());
            userCreatureMapper.insert(userCreature);
        } else {
            userCreature.setCount(userCreature.getCount() + 1);
            userCreature.setUpdateTime(LocalDateTime.now());
            userCreatureMapper.updateById(userCreature);
        }
    }
}
