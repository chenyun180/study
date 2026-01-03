package com.cloud.test.controller;

import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.constants.RedisConstants;
import com.cloud.common.redis.RedisCache;
import com.cloud.common.redis.RedisCacheUtil;
import com.cloud.test.config.ParamConfig;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@RestController
@Api(description = "测试接口Controller")
@RequestMapping("/person")
public class PersonController  {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Resource
    private RedisCacheUtil redisCacheUtil;
    @Resource
    private RedisCache redisCache;
    @Resource
    private ParamConfig paramConfig;


    @PostMapping("/testRedisScan")
    @ApiImplicitParams({
            @ApiImplicitParam(name="mobile",value="手机号",required=true,paramType="form"),
            @ApiImplicitParam(name="password",value="密码",required=true,paramType="form"),
            @ApiImplicitParam(name="age",value="年龄",required=true,paramType="form",dataType="Integer")
    })
    public void testRedisScan(@RequestBody JSONObject jsonObject){
        Set<String> set = redisCache.scan("test_scan_");
        set.forEach(System.out::println);
        for(String localKey : set) {
            redisCache.del(localKey);
        }
    }

    /**
     *  分布式锁
     *  第一个线程进来，获取到锁，睡眠10s,此时持有某行数据的锁；第二个进来，获取锁直接失败，返回。
     */
    @PostMapping("/testDistributedLock")
    public void testDistributedLock(@RequestBody JSONObject jsonObject) {
        String id = jsonObject.getString("id");
        String localKey = String.format(RedisConstants.TEST_DISTRIBUTED, id);
        String clientId = UUID.randomUUID().toString();
        boolean isSuccess = redisCacheUtil.tryGetDistributedLock(localKey, clientId, 30);

        if(!isSuccess){
            logger.error("加锁失败======");
            return;
        }
        logger.info("开始睡眠============10s");
        try{
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            logger.error("中断异常", e);
        }

        logger.info("睡眠时间到=======,开始解锁");

        System.out.println(redisCacheUtil.releaseDistributedLock(localKey, clientId));
    }


    /**
     * MybatisPlus乐观锁
     * 仅支持updateById(id)与update(Entity entity, Wrapper<T> updateWrapper)方法
     * 若使用后一个方法，updateWrapper不能复用
     * 若更新不成功，就会结束。不会重试。适合处理重复订单之类的业务。不适合所有请求依次调用数据库场景。
     */
    @PostMapping("/testOptimism")
    public String testOptimism(@RequestBody JSONObject jsonObject){
        logger.info("11111111");
        return null;
    }
    
    /**
     * 动态获取nacos配置中心中信息
     */
    @PostMapping("/getConfigParam")
    public String getConfigParam(String prefix) {
        String param1 = paramConfig.getParam1();
        String param2 = paramConfig.getParam2();

        logger.info("配置文件参数为：" + param1 + param2);
        return prefix + param1 + param2;
    }

}
