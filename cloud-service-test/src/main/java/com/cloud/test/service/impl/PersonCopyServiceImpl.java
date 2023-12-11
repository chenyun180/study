package com.cloud.test.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.common.config.CommonThreadPool;
import com.cloud.common.model.test.PersonCopy;
import com.cloud.common.utils.service.ResultData;
import com.cloud.test.mapper.PersonCopyMapper;
import com.cloud.test.service.IPersonCopyService;
import com.cloud.test.task.TestCommonThreadTask;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

@Service
public class PersonCopyServiceImpl extends ServiceImpl<PersonCopyMapper, PersonCopy> implements IPersonCopyService {

    private static final Logger logger = LoggerFactory.getLogger(PersonCopyServiceImpl.class);

    @Resource
    private CommonThreadPool commonThreadPool;

    @Override
    public ResultData<Boolean> savePerson(PersonCopy personCopy) {
        return null;
    }

    @Override
    public void testCommonThreadPool() {

        List<String> allDataList = new ArrayList<>();
        List<Callable<List<String>>> list = Lists.newArrayList();
        for(int i = 0; i < 5; i++){
            list.add(new TestCommonThreadTask(String.valueOf(i)));
        }
        try {
            List<Future<List<String>>> resultList = commonThreadPool.submitAll(list);

            for(Future<List<String>> futureTask : resultList) {
                List<String> future = futureTask.get();
                allDataList.addAll(future);
            }
            logger.info("all data list=" + JSONObject.toJSONString(allDataList));
        } catch (InterruptedException e) {
            logger.error("interrupted exception", e);
        } catch (ExecutionException e) {
            logger.error("execution exception", e);
        }

    }

}
