package com.horseracing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.horseracing.entity.Creature;
import com.horseracing.entity.User;
import com.horseracing.entity.UserYard;
import com.horseracing.mapper.CreatureMapper;
import com.horseracing.mapper.UserMapper;
import com.horseracing.mapper.UserYardMapper;
import com.horseracing.dto.YardPlaceDTO;
import com.horseracing.service.YardService;
import com.horseracing.vo.YardSlotVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 庭院服务实现
 *
 * @author rzf
 */
@Service
public class YardServiceImpl extends ServiceImpl<UserYardMapper, UserYard> implements YardService {

    @Autowired
    private UserYardMapper userYardMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CreatureMapper creatureMapper;

    @Override
    public List<YardSlotVO> getUserYard(Long userId) {
        LambdaQueryWrapper<UserYard> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserYard::getUserId, userId);
        wrapper.orderByAsc(UserYard::getSlotId);
        List<UserYard> list = userYardMapper.selectList(wrapper);

        List<YardSlotVO> result = new ArrayList<>();
        for (UserYard yard : list) {
            YardSlotVO vo = new YardSlotVO();
            vo.setId(yard.getId());
            vo.setSlotId(yard.getSlotId());
            vo.setCreatureId(yard.getCreatureId());
            vo.setIsUnlocked(yard.getIsUnlocked());

            if (yard.getCreatureId() != null) {
                Creature creature = creatureMapper.selectById(yard.getCreatureId());
                if (creature != null) {
                    vo.setCreatureName(creature.getName());
                    vo.setCreatureImageUrl(creature.getImageUrl());
                }
            }

            result.add(vo);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean placeCreature(YardPlaceDTO dto) {
        Long userId = dto.getUserId();
        Integer slotId = dto.getSlotId();
        Long creatureId = dto.getCreatureId();

        // 查询用户
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        // 查询槽位
        LambdaQueryWrapper<UserYard> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserYard::getUserId, userId);
        wrapper.eq(UserYard::getSlotId, slotId);
        UserYard yard = userYardMapper.selectOne(wrapper);

        if (yard == null || yard.getIsUnlocked() != 1) {
            return false;
        }

        // 更新放置
        yard.setCreatureId(creatureId);
        yard.setUpdateTime(LocalDateTime.now());
        userYardMapper.updateById(yard);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unlockSlot(Long userId, Integer slotId, Integer cost) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }

        // 检查饵料是否足够
        if (user.getCurrentBait() < cost) {
            return false;
        }

        // 查询槽位是否已存在
        LambdaQueryWrapper<UserYard> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserYard::getUserId, userId);
        wrapper.eq(UserYard::getSlotId, slotId);
        UserYard yard = userYardMapper.selectOne(wrapper);

        if (yard != null && yard.getIsUnlocked() == 1) {
            return true; // 已经解锁了
        }

        // 扣除饵料
        user.setCurrentBait(user.getCurrentBait() - cost);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);

        if (yard == null) {
            // 创建新槽位
            yard = new UserYard();
            yard.setUserId(userId);
            yard.setSlotId(slotId);
            yard.setIsUnlocked(1);
            yard.setCreatureId(null);
            yard.setCreateTime(LocalDateTime.now());
            yard.setUpdateTime(LocalDateTime.now());
            userYardMapper.insert(yard);
        } else {
            // 解锁已有槽位
            yard.setIsUnlocked(1);
            yard.setUpdateTime(LocalDateTime.now());
            userYardMapper.updateById(yard);
        }

        return true;
    }
}
