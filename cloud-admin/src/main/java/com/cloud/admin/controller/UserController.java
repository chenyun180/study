package com.cloud.admin.controller;

import com.cloud.admin.model.dto.UserInfoResponse;
import com.cloud.admin.service.SysUserService;
import com.cloud.common.constants.AuthConstants;
import com.cloud.common.model.base.ResultEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户控制器
 * 处理用户信息相关请求
 *
 * @author cloud
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Api(tags = "用户管理")
public class UserController {

    private final SysUserService sysUserService;

    /**
     * 获取当前登录用户信息
     *
     * @param httpRequest HTTP请求
     * @return 用户信息
     */
    @PostMapping("/info")
    @ApiOperation(value = "获取用户信息", notes = "通过JWT Token获取当前登录用户的详细信息")
    public ResultEntity<UserInfoResponse> getUserInfo(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader(AuthConstants.AUTHORIZATION_HEADER);
        log.info("获取用户信息请求");
        return sysUserService.getUserInfo(token);
    }
}

