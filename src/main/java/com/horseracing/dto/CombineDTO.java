package com.horseracing.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 合成请求DTO
 *
 * @author rzf
 */
@Data
public class CombineDTO {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 第一个合成生物ID
     */
    @NotNull(message = "第一个生物ID不能为空")
    private Long creatureId1;

    /**
     * 第二个合成生物ID
     */
    @NotNull(message = "第二个生物ID不能为空")
    private Long creatureId2;
}
