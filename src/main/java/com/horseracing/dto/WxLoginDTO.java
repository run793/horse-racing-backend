package com.horseracing.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 微信登录DTO
 *
 * @author rzf
 */
@Data
public class WxLoginDTO {

    @NotBlank(message = "code不能为空")
    private String code;

    /**
     * 用户昵称（前端传入）
     */
    private String nickname;

    /**
     * 头像URL（前端传入）
     */
    private String avatarUrl;
}
