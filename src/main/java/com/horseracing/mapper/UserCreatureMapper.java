package com.horseracing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.horseracing.entity.UserCreature;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户生物库存Mapper
 *
 * @author rzf
 */
@Mapper
public interface UserCreatureMapper extends BaseMapper<UserCreature> {
}
