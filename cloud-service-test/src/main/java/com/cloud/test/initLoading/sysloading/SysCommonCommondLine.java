package com.cloud.test.initLoading.sysloading;

import com.cloud.test.initLoading.sysloading.task.SysDeptLoadingTask;
import com.cloud.test.initLoading.sysloading.task.SysRoleLoadingTask;
import com.cloud.test.initLoading.sysloading.task.SysUserLoadingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@Order(1)
@Component
public class SysCommonCommondLine implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SysCommonCommondLine.class);

    CountDownLatch countDownLatch = new CountDownLatch(3);

    @Override
    public void run(String... args) throws Exception {

        logger.info("########################部门信息缓存开始加载#############################");
        List<Future<String>> futureList = new ArrayList<>();

        List<Callable<?>> reqList = new ArrayList<>();
        reqList.add(new SysDeptLoadingTask(countDownLatch));
        reqList.add(new SysRoleLoadingTask(countDownLatch));
        reqList.add(new SysUserLoadingTask(countDownLatch));

        reqList.forEach(req -> {
            new Thread(new FutureTask<>(req)).start();
        });

        try {
            countDownLatch.await();
            logger.info("########################部门信息缓存加载完成#############################");
        } catch (InterruptedException e) {
            logger.info("########################部门信息缓存加载异常########################");
        }

    }
}
