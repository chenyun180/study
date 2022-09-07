package com.cloud.test.initLoading.sysloading.task;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.constants.RedisConstants;
import com.cloud.common.redis.RedisCache;
import com.cloud.common.utils.SpringUtil;
import com.cloud.test.entity.SysRole;
import com.cloud.test.service.SysRoleService;
import com.cloud.test.service.impl.SysRoleServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class SysRoleLoadingTask implements Callable<String> {

    private static final Logger logger = LoggerFactory.getLogger(SysRoleLoadingTask.class);

    private CountDownLatch countDownLatch;

    public SysRoleLoadingTask(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }

    @Override
    public String call() throws Exception {
        String result = "";
        try{
            logger.info("================获取角色数据开始====================");
            SysRoleService sysRoleService = SpringUtil.getBean("sysRoleServiceImpl", SysRoleServiceImpl.class);
            List<SysRole> sysRoleList = sysRoleService.getAll();

            //存入redis
            RedisCache redisCache = SpringUtil.getBean("redisCache", RedisCache.class);
            redisCache.setString(String.format(RedisConstants.SYS_COMMON_PREFIX, "sysRole"), JSONObject.toJSONString(sysRoleList), 600);
            result = "success";
            logger.info("====================获取角色数据结束====================");
        } catch (Exception e){
            logger.error("======获取角色数据异常=====", e);
        }
        countDownLatch.countDown();
        return result;
    }

}
