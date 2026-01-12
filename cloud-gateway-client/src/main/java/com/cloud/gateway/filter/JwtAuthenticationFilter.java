package com.cloud.gateway.filter;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.cloud.common.constants.AuthConstants;
import com.cloud.common.model.base.ResultEntity;
import com.cloud.common.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * 网关JWT认证过滤器
 * 在网关层面验证JWT Token，拦截未认证请求
 *
 * @author cloud
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 路径匹配器
     */
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 白名单路径
     */
    private static final String[] WHITE_LIST = {
            "/api/auth/login",
            "/api/auth/refresh",
            "/api/auth/captcha",
            "/actuator/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/webjars/**",
            "/doc.html"
    };

    /**
     * 过滤器执行方法
     *
     * @param exchange 服务器Web交换对象
     * @param chain    网关过滤器链
     * @return Mono<Void>
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().value();

        log.debug("网关收到请求: {} {}", request.getMethod(), path);

        // 白名单路径直接放行
        if (isWhiteList(path)) {
            log.debug("白名单路径，直接放行: {}", path);
            return chain.filter(exchange);
        }

        // 获取Authorization请求头
        String authHeader = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION_HEADER);

        // 验证Token是否存在
        if (StrUtil.isBlank(authHeader) || !authHeader.startsWith(AuthConstants.TOKEN_PREFIX)) {
            log.warn("请求缺少有效的Authorization头: {}", path);
            return unauthorizedResponse(exchange, "未登录或登录已过期");
        }

        // 提取Token
        String token = JwtUtil.extractToken(authHeader);

        // 验证Token是否有效
        if (StrUtil.isBlank(token) || !JwtUtil.validateToken(token)) {
            log.warn("Token无效或已过期: {}", path);
            return unauthorizedResponse(exchange, "Token无效或已过期");
        }

        // 验证是否是Access Token
        if (!JwtUtil.isAccessToken(token)) {
            log.warn("Token类型错误，非Access Token: {}", path);
            return unauthorizedResponse(exchange, "Token类型错误");
        }

        // 检查Token是否在黑名单中
        String blacklistKey = String.format("auth:token_blacklist:%s", token);
        String blacklistValue = stringRedisTemplate.opsForValue().get(blacklistKey);
        if (StrUtil.isNotBlank(blacklistValue)) {
            log.warn("Token已被加入黑名单: {}", path);
            return unauthorizedResponse(exchange, "Token已失效，请重新登录");
        }

        // 获取用户信息
        Long userId = JwtUtil.getUserId(token);
        String username = JwtUtil.getUsername(token);

        if (userId == null || StrUtil.isBlank(username)) {
            log.warn("无法从Token中获取用户信息: {}", path);
            return unauthorizedResponse(exchange, "Token无效");
        }

        log.debug("Token验证通过，用户: {}, 路径: {}", username, path);

        // 将用户信息添加到请求头，传递给下游服务
        ServerHttpRequest mutatedRequest = request.mutate()
                .header("X-User-Id", String.valueOf(userId))
                .header("X-User-Name", username)
                .build();

        return chain.filter(exchange.mutate().request(mutatedRequest).build());
    }

    /**
     * 判断是否是白名单路径
     *
     * @param path 请求路径
     * @return 是否是白名单
     */
    private boolean isWhiteList(String path) {
        for (String pattern : WHITE_LIST) {
            if (pathMatcher.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回401未授权响应
     *
     * @param exchange 服务器Web交换对象
     * @param message  错误消息
     * @return Mono<Void>
     */
    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        ResultEntity<Object> result = new ResultEntity<>(
                HttpStatus.UNAUTHORIZED.value(),
                message,
                null
        );

        String body = JSON.toJSONString(result);
        DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }

    /**
     * 过滤器顺序
     * 数值越小优先级越高
     *
     * @return 顺序值
     */
    @Override
    public int getOrder() {
        return -100;
    }
}

