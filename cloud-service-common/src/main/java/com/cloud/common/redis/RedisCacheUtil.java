package com.cloud.common.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * @author: chenyun
 * @Date: 2020/7/2 9:35
 * @Description: redis实现的锁相关方法
 */
@Configuration
public class RedisCacheUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheUtil.class);

    private static final Long RELEASE_SUCCESS = 1L;
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "EX";
    private static final String RELEASE_LOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "return redis.call('del', KEYS[1]) else return 0 end";

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取分布式锁
     *
     * @param lockKey
     * @param clientId 加锁客户端唯一标识（使用UUID）
     * @param expireTime：单位-秒
     * @return
     * pom中对redis的依赖，需要排除Redis的异步客户端lettuce。详见pom配合，否则报错：java.lang.ClassCastException:
     * io.lettuce.core.RedisAsyncCommandsImpl cannot be cast to redis.clients.jedis.Jedis
     */
    public boolean tryGetDistributedLock(String lockKey, String clientId, long expireTime) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            String result = jedis.set(lockKey, clientId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
            if (LOCK_SUCCESS.equals(result)) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        });
    }

    /**
     * 释放分布式锁,与tryGetDistributedLock对应
     *
     * @param lockKey
     * @param clientId 需要与加锁clientId一致，否则解锁会失败
     * @return
     */
    public boolean releaseDistributedLock(String lockKey, String clientId) {

        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
            Jedis jedis = (Jedis) redisConnection.getNativeConnection();
            Object result = jedis.eval(RELEASE_LOCK_SCRIPT, Collections.singletonList(lockKey),
                    Collections.singletonList(clientId));
            if (RELEASE_SUCCESS.equals(result)) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        });
    }

}
