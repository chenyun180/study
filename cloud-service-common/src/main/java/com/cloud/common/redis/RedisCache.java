package com.cloud.common.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis
 */
@Component
public class RedisCache {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 根据key获取字符串类型的值
     *
     * @param key 数据的key值
     * @return 储存的内容
     */
    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 存值字符串类型
     *
     * @param key     储存的key值
     * @param value   需要储存的内容
     * @param seconds 有效时常，单位秒
     * @return boolean 是否储存成功，true成功，false失败
     */
    public boolean setString(String key, String value, long seconds) {
        return setString(key, value, seconds, TimeUnit.SECONDS);
    }

    /**
     * 存值字符串类型
     *
     * @param key      储存的key值
     * @param value    需要储存的内容
     * @param time     存储有效期
     * @param timeUnit 时间单元：秒（TimeUnit.SECONDS）、分（TimeUnit.MINUTES）、时（TimeUnit.HOURS）、天（（TimeUnit.DAYS））
     * @return
     */
    public boolean setString(String key, String value, long time, TimeUnit timeUnit) {
        if (timeUnit == null) {
            stringRedisTemplate.boundValueOps(key).set(value, time, TimeUnit.SECONDS);
        } else {
            stringRedisTemplate.boundValueOps(key).set(value, time, timeUnit);
        }
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire > 0;
    }


    /**
     * 删除数据
     *
     * @param key 需要删除的内容key
     * @return 只要没有出错只返回true
     */
    public boolean del(String key) {
        return stringRedisTemplate.delete(key);
    }


    /**
     * 获取指定对象
     *
     * @param key   需要获取的key
     * @param clazz 指定的clazz
     * @return 返回传入的类型对象
     */
    public <T> T getObject(String key, Class<T> clazz) {
        try {
            String string = getString(key);
            if(string==null){
                return null;
            }
            //转成对象
            return JSON.parseObject(string, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 存储对象Object
     *
     * @param key      储存的key值
     * @param obj      需要储存的对象
     * @param time     存储有效期
     * @param timeUnit 时间单元：秒（TimeUnit.SECONDS）、分（TimeUnit.MINUTES）、时（TimeUnit.HOURS）、天（（TimeUnit.DAYS））
     * @return
     */
    public <T> boolean setObject(String key, T obj, long time, TimeUnit timeUnit) {
        if (timeUnit == null) {
            setString(key, JSON.toJSONString(obj), time);
        } else {
            setString(key, JSON.toJSONString(obj), time, timeUnit);
        }
        Long expire = stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
        return expire > 0;
    }

    /**
     * 获取过期时间
     * @param key 需要查询的key
     * @return 距离过期剩余的时间（秒）
     */
    public Long  getExpire(String key){
       return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 获取所有相同前缀的key
     */
    public Set<String> scan(String matchKey) {
        Set<String> keys = stringRedisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(matchKey + "*")
                    .count(1000).build());
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next()));
            }
            return keysTmp;
        });

        return keys;
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return stringRedisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return 147
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return stringRedisTemplate.opsForValue().increment(key, -delta);
    }

}
