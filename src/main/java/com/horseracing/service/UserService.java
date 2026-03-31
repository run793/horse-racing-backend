package com.horseracing.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.horseracing.entity.User;

/**
 * 用户 Service 接口
 */
public interface UserService extends IService<User> {

    /**
     * 根据 openid 获取用户信息
     * @param openid 微信 openid
     * @return 用户信息
     */
    User getByOpenid(String openid);

    /**
     * 分页查询用户列表
     * @param current 当前页
     * @param size 每页大小
     * @return 分页结果
     */
    Page<User> pageList(long current, long size);
}
