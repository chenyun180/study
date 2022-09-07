package com.cloud.test.initLoading.sysloading.task;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.constants.RedisConstants;
import com.cloud.common.redis.RedisCache;
import com.cloud.common.utils.SpringUtil;
import com.cloud.test.entity.SysDept;
import com.cloud.test.service.SysDeptService;
import com.cloud.test.service.impl.SysDeptServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class SysDeptLoadingTask implements Callable<String> {

    private static final Logger logger = LoggerFactory.getLogger(SysDeptLoadingTask.class);

    private CountDownLatch countDownLatch;

    public SysDeptLoadingTask(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }

    @Override
    public String call() throws Exception {
        String result = "";
        try{
            logger.info("================获取部门数据开始====================");
            SysDeptService sysDeptService = SpringUtil.getBean("sysDeptServiceImpl", SysDeptServiceImpl.class);
            List<SysDept> sysDeptList = sysDeptService.getAll();

            //存入redis
            RedisCache redisCache = SpringUtil.getBean("redisCache", RedisCache.class);
            redisCache.setString(String.format(RedisConstants.SYS_COMMON_PREFIX, "sysDept"), JSONObject.toJSONString(sysDeptList), 600);

            String str = redisCache.getString(String.format(RedisConstants.SYS_COMMON_PREFIX, "sysDept"));
            logger.info("dept result =" + str);
            result = "success";
            logger.info("====================获取部门数据结束====================");
        } catch (Exception e){
            logger.error("====================获取部门数据异常====================", e);
        }
        countDownLatch.countDown();
        return result;
    }
}
