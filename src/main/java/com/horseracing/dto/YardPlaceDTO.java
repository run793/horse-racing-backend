package com.horseracing.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 庭院放置请求DTO
 *
 * @author rzf
 */
@Data
public class YardPlaceDTO {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 槽位ID
     */
    @NotNull(message = "槽位ID不能为空")
    private Integer slotId;

    /**
     * 生物ID，为空表示清空
     */
    private Long creatureId;
}
