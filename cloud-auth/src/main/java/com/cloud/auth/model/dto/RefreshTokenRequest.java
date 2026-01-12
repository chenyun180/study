package com.cloud.auth.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 刷新Token请求参数
 *
 * @author cloud
 */
@Data
@ApiModel(value = "RefreshTokenRequest", description = "刷新Token请求参数")
public class RefreshTokenRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Refresh Token
     */
    @NotBlank(message = "refreshToken不能为空")
    @ApiModelProperty(value = "Refresh Token", required = true, example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;
}

