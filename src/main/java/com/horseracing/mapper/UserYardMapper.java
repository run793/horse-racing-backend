package com.horseracing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.horseracing.entity.UserYard;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户庭院Mapper
 *
 * @author rzf
 */
@Mapper
public interface UserYardMapper extends BaseMapper<UserYard> {
}
