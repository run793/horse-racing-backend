package com.horseracing.vo;

import lombok.Data;

/**
 * 庭院槽位VO
 *
 * @author rzf
 */
@Data
public class YardSlotVO {

    private Long id;
    private Integer slotId;
    private Long creatureId;
    private String creatureName;
    private String creatureImageUrl;
    private Integer isUnlocked;
}
