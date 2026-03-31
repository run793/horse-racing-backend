package com.horseracing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.horseracing.entity.Creature;
import org.apache.ibatis.annotations.Mapper;

/**
 * 生物配置Mapper
 *
 * @author rzf
 */
@Mapper
public interface CreatureMapper extends BaseMapper<Creature> {
}
