package com.horseracing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.horseracing.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper 接口
 * 继承 BaseMapper 获得 MyBatis-Plus 提供的基础 CRUD 方法
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
