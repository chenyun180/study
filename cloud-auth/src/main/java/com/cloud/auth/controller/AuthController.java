package com.cloud.auth.controller;

import com.cloud.auth.model.dto.*;
import com.cloud.auth.service.AuthService;
import com.cloud.common.constants.AuthConstants;
import com.cloud.common.model.base.ResultEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 认证控制器
 * 处理用户登录、登出、Token刷新等认证相关请求
 *
 * @author cloud
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Api(tags = "认证管理")
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录
     *
     * @param request 登录请求参数
     * @return 登录响应（包含Token信息）
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "用户通过用户名和密码登录系统，返回JWT Token")
    public ResultEntity<LoginResponse> login(@RequestBody @Validated LoginRequest request) {
        log.info("用户登录请求，用户名: {}", request.getUsername());
        return authService.login(request);
    }

    /**
     * 用户登出
     *
     * @param httpRequest HTTP请求
     * @return 登出结果
     */
    @PostMapping("/logout")
    @ApiOperation(value = "用户登出", notes = "用户退出登录，清除服务端Token记录")
    public ResultEntity<Void> logout(HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader(AuthConstants.AUTHORIZATION_HEADER);
        log.info("用户登出请求");
        return authService.logout(token);
    }

    /**
     * 刷新Token
     *
     * @param request 刷新Token请求参数
     * @return 新的Access Token
     */
    @PostMapping("/refresh")
    @ApiOperation(value = "刷新Token", notes = "使用Refresh Token获取新的Access Token")
    public ResultEntity<RefreshTokenResponse> refreshToken(@RequestBody @Validated RefreshTokenRequest request) {
        log.info("刷新Token请求");
        return authService.refreshToken(request);
    }
}

