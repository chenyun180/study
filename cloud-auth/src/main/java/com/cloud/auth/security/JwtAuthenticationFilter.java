package com.cloud.auth.security;

import cn.hutool.core.util.StrUtil;
import com.cloud.common.constants.AuthConstants;
import com.cloud.common.constants.RedisConstants;
import com.cloud.common.redis.RedisCache;
import com.cloud.common.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT认证过滤器
 * 从请求头中提取Token并验证
 *
 * @author cloud
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final RedisCache redisCache;

    /**
     * 过滤请求
     *
     * @param request     请求
     * @param response    响应
     * @param filterChain 过滤器链
     * @throws ServletException Servlet异常
     * @throws IOException      IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 获取Authorization请求头
        String authHeader = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);

        // 如果没有Token，直接放行（交给后续过滤器处理权限）
        if (StrUtil.isBlank(authHeader) || !authHeader.startsWith(AuthConstants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 提取Token
        String token = JwtUtil.extractToken(authHeader);

        // 验证Token是否有效
        if (StrUtil.isBlank(token) || !JwtUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 验证是否是Access Token
        if (!JwtUtil.isAccessToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 检查Token是否在黑名单中
        String blacklistKey = String.format(RedisConstants.AUTH_TOKEN_BLACKLIST, token);
        String blacklistValue = redisCache.getString(blacklistKey);
        if (StrUtil.isNotBlank(blacklistValue)) {
            log.warn("Token在黑名单中: {}", token.substring(0, 20) + "...");
            filterChain.doFilter(request, response);
            return;
        }

        // 从Token中获取用户信息
        Long userId = JwtUtil.getUserId(token);
        String username = JwtUtil.getUsername(token);
        List<String> roles = JwtUtil.getRoles(token);

        if (userId == null || StrUtil.isBlank(username)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 构建权限列表
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        // 创建认证Token
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 设置到安全上下文
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        log.debug("JWT认证成功，用户: {}, 路径: {}", username, request.getRequestURI());

        filterChain.doFilter(request, response);
    }
}

