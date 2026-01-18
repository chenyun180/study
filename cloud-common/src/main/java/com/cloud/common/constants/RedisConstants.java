package com.cloud.common.constants;

/**
 * Redis缓存Key常量类
 *
 * @author cloud
 */
public class RedisConstants {

    //测试key
    public static final String TEST_KEY = "test_key_%s";

    //token相关
    public static final String USER_TOKEN = "user_token_%s";

    //测试分布式锁
    public static final String TEST_DISTRIBUTED = "test_distributed_%s";

    //sys系统相关数据
    public static final String SYS_COMMON_PREFIX = "sys_common_%s";

    // ==================== 认证相关 ====================

    /**
     * 用户Access Token缓存前缀，格式：auth:access_token:{userId}
     */
    public static final String AUTH_ACCESS_TOKEN = "auth:access_token:%s";

    /**
     * 用户Refresh Token缓存前缀，格式：auth:refresh_token:{userId}
     */
    public static final String AUTH_REFRESH_TOKEN = "auth:refresh_token:%s";

    /**
     * Token黑名单前缀，格式：auth:token_blacklist:{token}
     */
    public static final String AUTH_TOKEN_BLACKLIST = "auth:token_blacklist:%s";

    /**
     * 用户登录信息缓存前缀，格式：auth:user_info:{userId}
     */
    public static final String AUTH_USER_INFO = "auth:user_info:%s";

    /**
     * 登录失败次数限制前缀，格式：auth:login_fail:{username}
     */
    public static final String AUTH_LOGIN_FAIL_COUNT = "auth:login_fail:%s";

}
