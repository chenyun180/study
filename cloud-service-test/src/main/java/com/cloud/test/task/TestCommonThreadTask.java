package com.cloud.test.task;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

public class TestCommonThreadTask implements Callable<List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(TestCommonThreadTask.class);

    private String type;

    public TestCommonThreadTask(String type){
        this.type = type;
    }

    @Override
    public List<String> call() throws Exception {
        Thread.sleep(3000);
        logger.info("type=" + type);
        return Lists.newArrayList(type);
    }

}
