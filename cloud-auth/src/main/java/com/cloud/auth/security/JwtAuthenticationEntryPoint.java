package com.cloud.auth.security;

import com.alibaba.fastjson.JSON;
import com.cloud.common.model.base.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * JWT认证入口点
 * 处理未认证请求的响应
 *
 * @author cloud
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 当用户未认证时调用
     *
     * @param request       请求
     * @param response      响应
     * @param authException 认证异常
     * @throws IOException      IO异常
     * @throws ServletException Servlet异常
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.warn("未认证的请求: {} {}, 原因: {}",
                request.getMethod(), request.getRequestURI(), authException.getMessage());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ResultEntity<Object> result = new ResultEntity<>(
                HttpStatus.UNAUTHORIZED.value(),
                "未登录或登录已过期",
                null
        );

        response.getWriter().write(JSON.toJSONString(result));
    }
}

