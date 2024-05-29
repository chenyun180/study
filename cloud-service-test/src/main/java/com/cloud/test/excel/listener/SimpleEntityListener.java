package com.cloud.test.excel.listener;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.cloud.test.excel.entity.SimpleEntity;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SimpleEntityListener implements ReadListener<SimpleEntity> {

    private static final int BATCH_COUNT = 100;

    private List<SimpleEntity> cachedDataList = Lists.newArrayListWithExpectedSize(BATCH_COUNT);

    @Override
    public void invoke(SimpleEntity simpleEntity, AnalysisContext analysisContext) {
        log.info("解析到一条数据:{}", JSON.toJSONString(simpleEntity));
        cachedDataList.add(simpleEntity);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
//        if (cachedDataList.size() >= BATCH_COUNT) {
//            saveData();
//            // 存储完成清理 list
//            cachedDataList = Lists.newArrayListWithExpectedSize(BATCH_COUNT);
//        }
    }

    private void saveData() {

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }

    public List<SimpleEntity> getDataList() {
        return cachedDataList;
    }

}
