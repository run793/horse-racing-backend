package com.horseracing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录请求 DTO
 */
@Data
@ApiModel(description = "用户登录请求参数")
public class UserLoginDTO {

    @NotBlank(message = "code 不能为空")
    @ApiModelProperty(value = "微信登录code", required = true)
    private String code;
}
