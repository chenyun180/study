package com.cloud.auth.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 登录请求参数
 *
 * @author cloud
 */
@Data
@ApiModel(value = "LoginRequest", description = "登录请求参数")
public class LoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", required = true, example = "admin")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;

    /**
     * 是否记住我
     */
    @ApiModelProperty(value = "是否记住我", example = "true")
    private Boolean rememberMe = false;
}

