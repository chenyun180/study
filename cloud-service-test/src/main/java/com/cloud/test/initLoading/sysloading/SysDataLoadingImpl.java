package com.cloud.test.initLoading.sysloading;

import com.cloud.common.redis.RedisCache;
import com.cloud.test.initLoading.sysloading.task.SysDeptLoadingTask;
import com.cloud.test.initLoading.sysloading.task.SysRoleLoadingTask;
import com.cloud.test.initLoading.sysloading.task.SysUserLoadingTask;
import com.cloud.test.service.IPersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;


/**
 *  加载sys_role,sys_dept,sys_user三张表数据到缓存
 *  注意：使用@PostConstruct注解，加载时机较早，导致使用上下文去获取bean时，会有一些问题。建议这种情况使用CommondLine接口，参考SysCommonCommondLine
 */
@Service
@DependsOn("springUtil")
public class SysDataLoadingImpl implements SysDataLoading{

    private static final Logger logger = LoggerFactory.getLogger(SysDataLoadingImpl.class);

    CountDownLatch countDownLatch = new CountDownLatch(3);

    /**
     * postcontruct：在依赖完成后调用
     */
    @Override
    @PostConstruct
    public void initResource() {
//        logger.info("########################部门信息缓存开始加载#############################");
//        List<Future<String>> futureList = new ArrayList<>();
//
//        List<Callable<?>> reqList = new ArrayList<>();
//        reqList.add(new SysDeptLoadingTask(countDownLatch));
//        reqList.add(new SysRoleLoadingTask(countDownLatch));
//        reqList.add(new SysUserLoadingTask(countDownLatch));
//
//        reqList.forEach(req -> {
//            new Thread(new FutureTask<>(req)).start();
//        });
//
//        try {
//            countDownLatch.await();
//            logger.info("########################部门信息缓存加载完成#############################");
//        } catch (InterruptedException e) {
//            logger.info("########################部门信息缓存加载异常########################");
//        }

    }
}
