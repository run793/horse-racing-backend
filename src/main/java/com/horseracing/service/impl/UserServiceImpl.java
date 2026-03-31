package com.horseracing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.horseracing.entity.User;
import com.horseracing.mapper.UserMapper;
import com.horseracing.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户 Service 实现类
 * 继承 ServiceImpl 获得 MyBatis-Plus 提供的基础 CRUD 实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getByOpenid(String openid) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getOpenid, openid);
        return this.getOne(wrapper);
    }

    @Override
    public Page<User> pageList(long current, long size) {
        Page<User> page = new Page<>(current, size);
        return this.page(page);
    }
}
