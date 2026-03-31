package com.horseracing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.horseracing.entity.*;
import com.horseracing.mapper.*;
import com.horseracing.dto.CombineDTO;
import com.horseracing.service.CombineService;
import com.horseracing.service.UserService;
import com.horseracing.vo.CombineResultVO;
import com.horseracing.vo.CreatureInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 合成服务实现
 *
 * @author rzf
 */
@Service
public class CombineServiceImpl extends ServiceImpl<UserCreatureMapper, UserCreature> implements CombineService {

    @Autowired
    private CreatureMapper creatureMapper;

    @Autowired
    private UserCreatureMapper userCreatureMapper;

    @Autowired
    private UserIllustrationMapper userIllustrationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CombineResultVO combine(CombineDTO dto) {
        CombineResultVO result = new CombineResultVO();
        result.setSuccess(false);

        Long userId = dto.getUserId();
        Long cid1 = dto.getCreatureId1();
        Long cid2 = dto.getCreatureId2();

        // 1. 校验两个生物必须相同
        if (!cid1.equals(cid2)) {
            result.setMessage("两个生物必须相同才能合成");
            return result;
        }

        // 2. 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            result.setMessage("用户不存在");
            return result;
        }

        // 3. 查询用户该生物库存
        LambdaQueryWrapper<UserCreature> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCreature::getUserId, userId);
        wrapper.eq(UserCreature::getCreatureId, cid1);
        UserCreature userCreature = userCreatureMapper.selectOne(wrapper);

        if (userCreature == null || userCreature.getCount() < 2) {
            result.setMessage("该生物数量不足，需要两个才能合成");
            return result;
        }

        // 4. 查询当前生物信息
        Creature currentCreature = creatureMapper.selectById(cid1);
        if (currentCreature == null) {
            result.setMessage("生物不存在");
            return result;
        }

        // 5. 检查用户饵料是否足够
        int combineCost = currentCreature.getCombineCost();
        if (user.getCurrentBait() < combineCost) {
            result.setMessage("饵料不足，无法合成");
            return result;
        }

        // 6. 查询合成结果生物
        // 从配置中找出由这两个生物合成的结果
        // combineFrom 字段存储 "id1,id2"，匹配其中包含两个相同id
        String expectedCombineFrom = cid1 + "," + cid2;
        LambdaQueryWrapper<Creature> resultWrapper = new LambdaQueryWrapper<>();
        resultWrapper.eq(Creature::getCombineFrom, expectedCombineFrom);
        Creature resultCreature = creatureMapper.selectOne(resultWrapper);

        if (resultCreature == null) {
            result.setMessage("无法合成更高级的生物");
            return result;
        }

        // 7. 消耗两个低级生物 + 消耗饵料
        userCreature.setCount(userCreature.getCount() - 2);
        if (userCreature.getCount() <= 0) {
            userCreatureMapper.deleteById(userCreature.getId());
        } else {
            userCreatureMapper.updateById(userCreature);
        }

        // 扣除饵料
        user.setCurrentBait(user.getCurrentBait() - combineCost);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);

        // 8. 添加合成结果生物到用户库存
        LambdaQueryWrapper<UserCreature> resultCreatureWrapper = new LambdaQueryWrapper<>();
        resultCreatureWrapper.eq(UserCreature::getUserId, userId);
        resultCreatureWrapper.eq(UserCreature::getCreatureId, resultCreature.getId());
        UserCreature resultUserCreature = userCreatureMapper.selectOne(resultCreatureWrapper);

        if (resultUserCreature == null) {
            resultUserCreature = new UserCreature();
            resultUserCreature.setUserId(userId);
            resultUserCreature.setCreatureId(resultCreature.getId());
            resultUserCreature.setCount(1);
            resultUserCreature.setIsUnlocked(0);
            resultUserCreature.setCreateTime(LocalDateTime.now());
            resultUserCreature.setUpdateTime(LocalDateTime.now());
            userCreatureMapper.insert(resultUserCreature);
        } else {
            resultUserCreature.setCount(resultUserCreature.getCount() + 1);
            resultUserCreature.setUpdateTime(LocalDateTime.now());
            userCreatureMapper.updateById(resultUserCreature);
        }

        // 9. 点亮图鉴（如果未点亮）
        boolean isNewUnlock = false;
        LambdaQueryWrapper<UserIllustration> illusWrapper = new LambdaQueryWrapper<>();
        illusWrapper.eq(UserIllustration::getUserId, userId);
        illusWrapper.eq(UserIllustration::getCreatureId, resultCreature.getId());
        UserIllustration illustration = userIllustrationMapper.selectOne(illusWrapper);

        if (illustration == null) {
            illustration = new UserIllustration();
            illustration.setUserId(userId);
            illustration.setCreatureId(resultCreature.getId());
            illustration.setUnlockTime(LocalDateTime.now());
            illustration.setCreateTime(LocalDateTime.now());
            illustration.setUpdateTime(LocalDateTime.now());
            userIllustrationMapper.insert(illustration);
            isNewUnlock = true;

            // 标记库存已解锁
            if (resultUserCreature != null) {
                resultUserCreature.setIsUnlocked(1);
                userCreatureMapper.updateById(resultUserCreature);
            }
        }

        // 10. 检查是否升级最大等级
        boolean levelUpgraded = false;
        if (resultCreature.getLevel() > user.getMaxLevel()) {
            user.setMaxLevel(resultCreature.getLevel());
            userMapper.updateById(user);
            levelUpgraded = true;
        }

        // 11. 返回结果
        result.setSuccess(true);
        result.setResultCreatureId(resultCreature.getId());
        result.setResultCreatureName(resultCreature.getName());
        result.setIsNewUnlock(isNewUnlock);
        result.setLevelUpgraded(levelUpgraded);
        result.setRemainingBait(user.getCurrentBait());

        return result;
    }

    @Override
    public List<CreatureInfoVO> getUserCreatureList(Long userId) {
        // 查询用户所有库存
        LambdaQueryWrapper<UserCreature> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserCreature::getUserId, userId);
        List<UserCreature> list = userCreatureMapper.selectList(wrapper);

        List<CreatureInfoVO> result = new ArrayList<>();
        for (UserCreature uc : list) {
            Creature creature = creatureMapper.selectById(uc.getCreatureId());
            if (creature != null) {
                CreatureInfoVO vo = convert(creature);
                result.add(vo);
            }
        }

        return result;
    }

    @Override
    public List<CreatureInfoVO> getAllCreatureList() {
        List<Creature> list = creatureMapper.selectList(null);
        return list.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unlockSeaArea(Long userId, Integer seaAreaId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        // 检查是否已经解锁
        if (user.getUnlockedSeaArea() >= seaAreaId) {
            return true;
        }

        // 检查前一个海域是否已经解锁
        if (user.getUnlockedSeaArea() < seaAreaId - 1) {
            return false;
        }

        // 解锁海域
        user.setUnlockedSeaArea(seaAreaId);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);

        return true;
    }

    /**
     * 转换为VO
     */
    private CreatureInfoVO convert(Creature creature) {
        CreatureInfoVO vo = new CreatureInfoVO();
        vo.setId(creature.getId());
        vo.setName(creature.getName());
        vo.setLevel(creature.getLevel());
        vo.setSeaArea(creature.getSeaArea());
        vo.setImageUrl(creature.getImageUrl());
        vo.setDescription(creature.getDescription());
        vo.setIsMythical(creature.getIsMythical());
        vo.setCombineCost(creature.getCombineCost());
        vo.setCombineFrom(creature.getCombineFrom());
        return vo;
    }
}
