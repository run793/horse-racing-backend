package com.horseracing.vo;

import lombok.Data;

/**
 * 生物信息VO
 *
 * @author rzf
 */
@Data
public class CreatureInfoVO {

    private Long id;
    private String name;
    private Integer level;
    private Integer seaArea;
    private String imageUrl;
    private String description;
    private Integer isMythical;
    private Integer combineCost;
    private String combineFrom;
}
