package com.horseracing.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息VO
 *
 * @author rzf
 */
@Data
public class UserInfoVO {

    private Long userId;
    private String openid;
    private String nickname;
    private String avatarUrl;
    private Integer currentBait;
    private Integer unlockedSeaArea;
    private Integer maxLevel;
    private LocalDateTime lastStealTime;
    private Boolean canStealToday;
    private Long offlineIncome;
    private Integer unlockedSlots;
}
