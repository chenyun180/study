package com.cloud.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import com.cloud.common.constants.AuthConstants;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT工具类
 * 用于生成、解析、验证JWT Token
 *
 * @author cloud
 */
@Slf4j
public class JwtUtil {

    /**
     * 密钥
     */
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            AuthConstants.JWT_SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * 生成Access Token
     *
     * @param userId     用户ID
     * @param username   用户名
     * @param roles      角色列表
     * @param rememberMe 是否记住我
     * @return Access Token
     */
    public static String generateAccessToken(Long userId, String username, List<String> roles, boolean rememberMe) {
        long expireTime = rememberMe ? AuthConstants.ACCESS_TOKEN_EXPIRE_REMEMBER : AuthConstants.ACCESS_TOKEN_EXPIRE;
        return generateToken(userId, username, roles, AuthConstants.TOKEN_TYPE_ACCESS, expireTime);
    }

    /**
     * 生成Refresh Token
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param roles    角色列表
     * @return Refresh Token
     */
    public static String generateRefreshToken(Long userId, String username, List<String> roles) {
        return generateToken(userId, username, roles, AuthConstants.TOKEN_TYPE_REFRESH, AuthConstants.REFRESH_TOKEN_EXPIRE);
    }

    /**
     * 生成Token
     *
     * @param userId        用户ID
     * @param username      用户名
     * @param roles         角色列表
     * @param tokenType     Token类型（access/refresh）
     * @param expireSeconds 过期时间（秒）
     * @return Token
     */
    private static String generateToken(Long userId, String username, List<String> roles,
                                         String tokenType, long expireSeconds) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expireSeconds * 1000);

        return Jwts.builder()
                .setIssuer(AuthConstants.JWT_ISSUER)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .claim(AuthConstants.CLAIM_USER_ID, userId)
                .claim(AuthConstants.CLAIM_USERNAME, username)
                .claim(AuthConstants.CLAIM_ROLES, roles)
                .claim(AuthConstants.CLAIM_TOKEN_TYPE, tokenType)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 解析Token获取Claims
     *
     * @param token JWT Token
     * @return Claims
     */
    public static Claims parseToken(String token) {
        if (StrUtil.isBlank(token)) {
            return null;
        }
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Token已过期: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Token解析失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 验证Token是否有效
     *
     * @param token JWT Token
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        return parseToken(token) != null;
    }

    /**
     * 从Token中获取用户ID
     *
     * @param token JWT Token
     * @return 用户ID
     */
    public static Long getUserId(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        Object userId = claims.get(AuthConstants.CLAIM_USER_ID);
        if (userId instanceof Integer) {
            return ((Integer) userId).longValue();
        }
        return (Long) userId;
    }

    /**
     * 从Token中获取用户名
     *
     * @param token JWT Token
     * @return 用户名
     */
    public static String getUsername(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        return (String) claims.get(AuthConstants.CLAIM_USERNAME);
    }

    /**
     * 从Token中获取角色列表
     *
     * @param token JWT Token
     * @return 角色列表
     */
    @SuppressWarnings("unchecked")
    public static List<String> getRoles(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        return (List<String>) claims.get(AuthConstants.CLAIM_ROLES);
    }

    /**
     * 从Token中获取Token类型
     *
     * @param token JWT Token
     * @return Token类型
     */
    public static String getTokenType(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        return (String) claims.get(AuthConstants.CLAIM_TOKEN_TYPE);
    }

    /**
     * 判断Token是否是Access Token
     *
     * @param token JWT Token
     * @return 是否是Access Token
     */
    public static boolean isAccessToken(String token) {
        return AuthConstants.TOKEN_TYPE_ACCESS.equals(getTokenType(token));
    }

    /**
     * 判断Token是否是Refresh Token
     *
     * @param token JWT Token
     * @return 是否是Refresh Token
     */
    public static boolean isRefreshToken(String token) {
        return AuthConstants.TOKEN_TYPE_REFRESH.equals(getTokenType(token));
    }

    /**
     * 获取Token过期时间
     *
     * @param token JWT Token
     * @return 过期时间
     */
    public static Date getExpiration(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        return claims.getExpiration();
    }

    /**
     * 判断Token是否即将过期（5分钟内）
     *
     * @param token JWT Token
     * @return 是否即将过期
     */
    public static boolean isTokenExpiringSoon(String token) {
        Date expiration = getExpiration(token);
        if (expiration == null) {
            return true;
        }
        // 5分钟 = 300000毫秒
        return expiration.getTime() - System.currentTimeMillis() < 300000;
    }

    /**
     * 从请求头中提取Token
     *
     * @param authorizationHeader Authorization请求头
     * @return Token
     */
    public static String extractToken(String authorizationHeader) {
        if (StrUtil.isBlank(authorizationHeader)) {
            return null;
        }
        if (authorizationHeader.startsWith(AuthConstants.TOKEN_PREFIX)) {
            return authorizationHeader.substring(AuthConstants.TOKEN_PREFIX.length());
        }
        return authorizationHeader;
    }

    private JwtUtil() {
        // 私有构造函数，防止实例化
    }
}
