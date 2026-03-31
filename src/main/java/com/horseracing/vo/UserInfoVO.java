package com.horseracing.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户信息响应 VO
 */
@Data
@ApiModel(description = "用户信息响应")
public class UserInfoVO {

    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "用户金币数")
    private Integer coins;

    @ApiModelProperty(value = "总对局数")
    private Integer totalGames;

    @ApiModelProperty(value = "获胜次数")
    private Integer winGames;

    @ApiModelProperty(value = "胜率")
    private Double winRate;
}
