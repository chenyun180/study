package com.cloud.common.constants;

/**
 * 认证相关常量类
 *
 * @author cloud
 */
public class AuthConstants {

    /**
     * JWT密钥（生产环境应从配置中心读取）
     */
    public static final String JWT_SECRET = "CloudStudySecretKey2024!@#$%^&*()_+";

    /**
     * JWT签发者
     */
    public static final String JWT_ISSUER = "cloud-auth";

    /**
     * Access Token有效期（秒）- 2小时
     */
    public static final long ACCESS_TOKEN_EXPIRE = 7200L;

    /**
     * Access Token有效期（秒）- 记住我模式7天
     */
    public static final long ACCESS_TOKEN_EXPIRE_REMEMBER = 604800L;

    /**
     * Refresh Token有效期（秒）- 7天
     */
    public static final long REFRESH_TOKEN_EXPIRE = 604800L;

    /**
     * Token类型
     */
    public static final String TOKEN_TYPE = "Bearer";

    /**
     * 请求头中的Authorization
     */
    public static final String AUTHORIZATION_HEADER = "Authorization";

    /**
     * Token前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 用户ID在JWT中的key
     */
    public static final String CLAIM_USER_ID = "userId";

    /**
     * 用户名在JWT中的key
     */
    public static final String CLAIM_USERNAME = "username";

    /**
     * 角色在JWT中的key
     */
    public static final String CLAIM_ROLES = "roles";

    /**
     * Token类型在JWT中的key
     */
    public static final String CLAIM_TOKEN_TYPE = "tokenType";

    /**
     * Access Token类型标识
     */
    public static final String TOKEN_TYPE_ACCESS = "access";

    /**
     * Refresh Token类型标识
     */
    public static final String TOKEN_TYPE_REFRESH = "refresh";

    /**
     * 登录失败最大次数
     */
    public static final int MAX_LOGIN_FAIL_COUNT = 5;

    /**
     * 登录失败锁定时间（秒）- 30分钟
     */
    public static final long LOGIN_FAIL_LOCK_TIME = 1800L;

    /**
     * 网关白名单路径
     */
    public static final String[] WHITE_LIST = {
            "/api/auth/login",
            "/api/auth/refresh",
            "/api/auth/captcha",
            "/actuator/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/webjars/**",
            "/doc.html"
    };

    private AuthConstants() {
        // 私有构造函数，防止实例化
    }
}

