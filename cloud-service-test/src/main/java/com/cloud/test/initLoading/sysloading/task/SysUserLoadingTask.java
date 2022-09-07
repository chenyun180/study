package com.cloud.test.initLoading.sysloading.task;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.constants.RedisConstants;
import com.cloud.common.redis.RedisCache;
import com.cloud.common.utils.SpringUtil;
import com.cloud.test.entity.SysUser;
import com.cloud.test.service.SysUserService;
import com.cloud.test.service.impl.SysUserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class SysUserLoadingTask implements Callable<String> {

    private static final Logger logger = LoggerFactory.getLogger(SysUserLoadingTask.class);

    private CountDownLatch countDownLatch;

    public SysUserLoadingTask(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }

    @Override
    public String call() throws Exception {
        String result = "";
        try{
            logger.info("================获取用户数据开始====================");
            SysUserService sysUserService = SpringUtil.getBean("sysUserServiceImpl", SysUserServiceImpl.class);
            List<SysUser> sysUserList = sysUserService.getAll();

            //存入redis
            RedisCache redisCache = SpringUtil.getBean("redisCache", RedisCache.class);
            redisCache.setString(String.format(RedisConstants.SYS_COMMON_PREFIX, "sysUser"), JSONObject.toJSONString(sysUserList), 600);
            result = "success";
            logger.info("====================获取用户数据结束====================");
        } catch (Exception e){
            logger.error("======获取用户数据异常====", e);
        }
        countDownLatch.countDown();
        return result;
    }
}
