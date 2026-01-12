package com.cloud.auth.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录响应参数
 *
 * @author cloud
 */
@Data
@Builder
@ApiModel(value = "LoginResponse", description = "登录响应参数")
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * JWT Access Token
     */
    @ApiModelProperty(value = "Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    /**
     * JWT Refresh Token
     */
    @ApiModelProperty(value = "Refresh Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    /**
     * Token过期时间（秒）
     */
    @ApiModelProperty(value = "Token过期时间（秒）", example = "7200")
    private Long expiresIn;

    /**
     * Token类型
     */
    @ApiModelProperty(value = "Token类型", example = "Bearer")
    private String tokenType;
}

