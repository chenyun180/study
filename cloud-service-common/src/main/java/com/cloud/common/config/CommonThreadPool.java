package com.cloud.common.config;

import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.*;

@Configuration
public class CommonThreadPool implements AutoCloseable {


    public static final Integer SIZE = 20;

    ExecutorService executorService = Executors.newFixedThreadPool(SIZE);


    public Future<?> submit(Callable<?> task){
        return executorService.submit(task);
    }

    public <T>List<Future<T>> submitAll(List<Callable<T>> taskList) throws InterruptedException {
        return executorService.invokeAll(taskList);
    }

    @Override
    public void close() throws Exception {
        if(null != executorService) {
            executorService.shutdown();
        }
    }

}
