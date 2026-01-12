package com.cloud.admin.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息响应参数
 *
 * @author cloud
 */
@Data
@Builder
@ApiModel(value = "UserInfoResponse", description = "用户信息响应参数")
public class UserInfoResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", example = "1")
    private Long id;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称", example = "系统管理员")
    private String name;

    /**
     * 用户邮箱
     */
    @ApiModelProperty(value = "用户邮箱", example = "admin@example.com")
    private String email;

    /**
     * 用户头像URL
     */
    @ApiModelProperty(value = "用户头像URL", example = "https://avatars.githubusercontent.com/u/1?v=4")
    private String avatar;

    /**
     * 用户角色列表
     */
    @ApiModelProperty(value = "用户角色列表", example = "[\"admin\", \"user\"]")
    private List<String> roles;

    /**
     * 用户权限列表
     */
    @ApiModelProperty(value = "用户权限列表", example = "[\"*:*:*\"]")
    private List<String> permissions;
}

