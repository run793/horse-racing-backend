package com.horseracing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.horseracing.entity.Creature;
import com.horseracing.entity.UserIllustration;
import com.horseracing.mapper.CreatureMapper;
import com.horseracing.mapper.UserIllustrationMapper;
import com.horseracing.service.IllustrationService;
import com.horseracing.vo.CreatureInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图鉴服务实现
 *
 * @author rzf
 */
@Service
public class IllustrationServiceImpl extends ServiceImpl<UserIllustrationMapper, UserIllustration> implements IllustrationService {

    @Autowired
    private UserIllustrationMapper userIllustrationMapper;

    @Autowired
    private CreatureMapper creatureMapper;

    @Override
    public List<CreatureInfoVO> getUserIllustrationList(Long userId) {
        // 查询用户已点亮的图鉴
        LambdaQueryWrapper<UserIllustration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserIllustration::getUserId, userId);
        List<UserIllustration> list = userIllustrationMapper.selectList(wrapper);

        List<CreatureInfoVO> result = new ArrayList<>();
        for (UserIllustration ui : list) {
            Creature creature = creatureMapper.selectById(ui.getCreatureId());
            if (creature != null) {
                CreatureInfoVO vo = convert(creature);
                result.add(vo);
            }
        }

        return result;
    }

    @Override
    public int[] getUserIllustrationStats(Long userId) {
        // 总生物数量
        long total = creatureMapper.selectCount(null);

        // 用户已点亮数量
        LambdaQueryWrapper<UserIllustration> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserIllustration::getUserId, userId);
        long unlocked = userIllustrationMapper.selectCount(wrapper);

        return new int[]{(int) unlocked, (int) total};
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
