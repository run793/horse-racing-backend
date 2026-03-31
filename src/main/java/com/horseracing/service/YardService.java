package com.horseracing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.horseracing.entity.UserYard;
import com.horseracing.dto.YardPlaceDTO;
import com.horseracing.vo.YardSlotVO;

import java.util.List;

/**
 * 庭院服务接口
 *
 * @author rzf
 */
public interface YardService extends IService<UserYard> {

    /**
     * 获取用户庭院所有槽位
     *
     * @param userId 用户ID
     * @return 槽位列表
     */
    List<YardSlotVO> getUserYard(Long userId);

    /**
     * 放置/更换神兽
     *
     * @param dto 放置请求
     * @return 是否成功
     */
    Boolean placeCreature(YardPlaceDTO dto);

    /**
     * 解锁槽位
     *
     * @param userId 用户ID
     * @param slotId 槽位ID
     * @param cost 需要消耗的饵料
     * @return 是否成功
     */
    Boolean unlockSlot(Long userId, Integer slotId, Integer cost);
}
