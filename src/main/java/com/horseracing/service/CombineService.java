package com.horseracing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.horseracing.entity.UserCreature;
import com.horseracing.dto.CombineDTO;
import com.horseracing.vo.CombineResultVO;
import com.horseracing.vo.CreatureInfoVO;

import java.util.List;

/**
 * 合成服务接口
 *
 * @author rzf
 */
public interface CombineService {

    /**
     * 合成操作
     *
     * @param dto 合成请求
     * @return 合成结果
     */
    CombineResultVO combine(CombineDTO dto);

    /**
     * 获取用户所有已解锁生物列表
     *
     * @param userId 用户ID
     * @return 生物列表
     */
    List<CreatureInfoVO> getUserCreatureList(Long userId);

    /**
     * 获取全量生物配置列表
     *
     * @return 生物列表
     */
    List<CreatureInfoVO> getAllCreatureList();

    /**
     * 解锁海域
     *
     * @param userId 用户ID
     * @param seaAreaId 海域ID
     * @return 是否解锁成功
     */
    Boolean unlockSeaArea(Long userId, Integer seaAreaId);
}
