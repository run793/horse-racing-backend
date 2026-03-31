package com.horseracing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.horseracing.entity.User;
import com.horseracing.dto.WxLoginDTO;
import com.horseracing.vo.UserInfoVO;

/**
 * 用户服务接口
 *
 * @author rzf
 */
public interface UserService extends IService<User> {

    /**
     * 微信登录
     *
     * @param dto 登录参数
     * @return 用户信息
     */
    UserInfoVO wxLogin(WxLoginDTO dto);

    /**
     * 获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoVO getUserInfo(Long userId);

    /**
     * 领取离线收益
     *
     * @param userId 用户ID
     * @return 获得的饵料数量
     */
    Integer collectOfflineIncome(Long userId);
}
