package com.cloud.auth.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 刷新Token响应参数
 *
 * @author cloud
 */
@Data
@Builder
@ApiModel(value = "RefreshTokenResponse", description = "刷新Token响应参数")
public class RefreshTokenResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 新的Access Token
     */
    @ApiModelProperty(value = "新的Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    /**
     * Token过期时间（秒）
     */
    @ApiModelProperty(value = "Token过期时间（秒）", example = "7200")
    private Long expiresIn;
}

