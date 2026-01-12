package com.cloud.auth.service;

import cn.hutool.core.util.StrUtil;
import com.cloud.auth.model.dto.LoginRequest;
import com.cloud.auth.model.dto.LoginResponse;
import com.cloud.auth.model.dto.RefreshTokenRequest;
import com.cloud.auth.model.dto.RefreshTokenResponse;
import com.cloud.auth.security.LoginUser;
import com.cloud.common.constants.AuthConstants;
import com.cloud.common.constants.RedisConstants;
import com.cloud.common.model.base.ResultEntity;
import com.cloud.common.redis.RedisCache;
import com.cloud.common.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务类
 *
 * @author cloud
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final RedisCache redisCache;

    /**
     * 用户登录
     *
     * @param request 登录请求参数
     * @return 登录响应
     */
    public ResultEntity<LoginResponse> login(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        Boolean rememberMe = request.getRememberMe() != null && request.getRememberMe();

        log.info("用户登录请求，用户名: {}", username);

        // 检查登录失败次数
        String failCountKey = String.format(RedisConstants.AUTH_LOGIN_FAIL_COUNT, username);
        String failCountStr = redisCache.getString(failCountKey);
        int failCount = StrUtil.isBlank(failCountStr) ? 0 : Integer.parseInt(failCountStr);

        if (failCount >= AuthConstants.MAX_LOGIN_FAIL_COUNT) {
            log.warn("用户 {} 登录失败次数过多，已被锁定", username);
            return ResultEntity.failed("登录失败次数过多，请" + (AuthConstants.LOGIN_FAIL_LOCK_TIME / 60) + "分钟后重试");
        }

        try {
            // 认证用户
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            // 获取认证用户信息
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();

            // 生成Token
            Long userId = loginUser.getUserId();
            List<String> roles = loginUser.getRoles();

            String accessToken = JwtUtil.generateAccessToken(userId, username, roles, rememberMe);
            String refreshToken = JwtUtil.generateRefreshToken(userId, username, roles);

            // 计算过期时间
            long expiresIn = rememberMe ? AuthConstants.ACCESS_TOKEN_EXPIRE_REMEMBER : AuthConstants.ACCESS_TOKEN_EXPIRE;

            // 缓存Token到Redis
            String accessTokenKey = String.format(RedisConstants.AUTH_ACCESS_TOKEN, userId);
            String refreshTokenKey = String.format(RedisConstants.AUTH_REFRESH_TOKEN, userId);
            redisCache.setString(accessTokenKey, accessToken, expiresIn, TimeUnit.SECONDS);
            redisCache.setString(refreshTokenKey, refreshToken, AuthConstants.REFRESH_TOKEN_EXPIRE, TimeUnit.SECONDS);

            // 清除登录失败次数
            redisCache.del(failCountKey);

            log.info("用户 {} 登录成功", username);

            LoginResponse response = LoginResponse.builder()
                    .token(accessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(expiresIn)
                    .tokenType(AuthConstants.TOKEN_TYPE)
                    .build();

            return ResultEntity.ok(response, "登录成功");

        } catch (BadCredentialsException e) {
            log.warn("用户 {} 登录失败，用户名或密码错误", username);
            // 增加登录失败次数
            redisCache.setString(failCountKey, String.valueOf(failCount + 1),
                    AuthConstants.LOGIN_FAIL_LOCK_TIME, TimeUnit.SECONDS);
            return ResultEntity.failed("用户名或密码错误");

        } catch (DisabledException e) {
            log.warn("用户 {} 登录失败，账号已被禁用", username);
            return ResultEntity.failed("账号已被禁用");

        } catch (Exception e) {
            log.error("用户 {} 登录异常: {}", username, e.getMessage(), e);
            return ResultEntity.failed("登录失败，请稍后重试");
        }
    }

    /**
     * 用户登出
     *
     * @param token Access Token
     * @return 登出结果
     */
    public ResultEntity<Void> logout(String token) {
        if (StrUtil.isBlank(token)) {
            return ResultEntity.ok(null, "登出成功");
        }

        // 提取实际Token
        token = JwtUtil.extractToken(token);

        Long userId = JwtUtil.getUserId(token);
        if (userId != null) {
            log.info("用户 {} 登出", userId);

            // 删除Redis中的Token
            String accessTokenKey = String.format(RedisConstants.AUTH_ACCESS_TOKEN, userId);
            String refreshTokenKey = String.format(RedisConstants.AUTH_REFRESH_TOKEN, userId);
            redisCache.del(accessTokenKey);
            redisCache.del(refreshTokenKey);

            // 将Token加入黑名单（剩余有效期内）
            Date expiration = JwtUtil.getExpiration(token);
            if (expiration != null) {
                long remainingTime = (expiration.getTime() - System.currentTimeMillis()) / 1000;
                if (remainingTime > 0) {
                    String blacklistKey = String.format(RedisConstants.AUTH_TOKEN_BLACKLIST, token);
                    redisCache.setString(blacklistKey, "1", remainingTime, TimeUnit.SECONDS);
                }
            }

            // 清除安全上下文
            SecurityContextHolder.clearContext();
        }

        return ResultEntity.ok(null, "登出成功");
    }

    /**
     * 刷新Token
     *
     * @param request 刷新Token请求
     * @return 刷新Token响应
     */
    public ResultEntity<RefreshTokenResponse> refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        log.info("刷新Token请求");

        // 验证Refresh Token
        if (!JwtUtil.validateToken(refreshToken)) {
            log.warn("Refresh Token无效或已过期");
            return ResultEntity.failed("Refresh Token无效或已过期");
        }

        // 验证是否是Refresh Token
        if (!JwtUtil.isRefreshToken(refreshToken)) {
            log.warn("Token类型错误，非Refresh Token");
            return ResultEntity.failed("Token类型错误");
        }

        // 检查Token是否在黑名单中
        String blacklistKey = String.format(RedisConstants.AUTH_TOKEN_BLACKLIST, refreshToken);
        String blacklistValue = redisCache.getString(blacklistKey);
        if (StrUtil.isNotBlank(blacklistValue)) {
            log.warn("Refresh Token在黑名单中");
            return ResultEntity.failed("Refresh Token已失效");
        }

        // 获取用户信息
        Long userId = JwtUtil.getUserId(refreshToken);
        String username = JwtUtil.getUsername(refreshToken);
        List<String> roles = JwtUtil.getRoles(refreshToken);

        if (userId == null || StrUtil.isBlank(username)) {
            log.warn("无法从Refresh Token中获取用户信息");
            return ResultEntity.failed("Refresh Token无效");
        }

        // 生成新的Access Token
        String newAccessToken = JwtUtil.generateAccessToken(userId, username, roles, false);
        long expiresIn = AuthConstants.ACCESS_TOKEN_EXPIRE;

        // 更新Redis中的Access Token
        String accessTokenKey = String.format(RedisConstants.AUTH_ACCESS_TOKEN, userId);
        redisCache.setString(accessTokenKey, newAccessToken, expiresIn, TimeUnit.SECONDS);

        log.info("用户 {} Token刷新成功", username);

        RefreshTokenResponse response = RefreshTokenResponse.builder()
                .token(newAccessToken)
                .expiresIn(expiresIn)
                .build();

        return ResultEntity.ok(response, "Token刷新成功");
    }

}

