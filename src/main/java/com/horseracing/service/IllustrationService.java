package com.horseracing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.horseracing.entity.UserIllustration;
import com.horseracing.vo.CreatureInfoVO;

import java.util.List;

/**
 * 图鉴服务接口
 *
 * @author rzf
 */
public interface IllustrationService extends IService<UserIllustration> {

    /**
     * 获取用户已点亮图鉴列表
     *
     * @param userId 用户ID
     * @return 图鉴列表
     */
    List<CreatureInfoVO> getUserIllustrationList(Long userId);

    /**
     * 获取用户图鉴点亮统计
     *
     * @param userId 用户ID
     * @return 已点亮数量 / 总数量
     */
    int[] getUserIllustrationStats(Long userId);
}
