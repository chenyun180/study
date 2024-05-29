package com.cloud.test.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.HashMap;
import java.util.Map;

public class AnalysisSimpleListener extends AnalysisEventListener<Map<Integer, String>> {

    private Map<Integer, Map<Integer, String>> map;
    private Map<Integer, String> titleMap = new HashMap<>();

    public AnalysisSimpleListener() {
        this.map = new HashMap<>();
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        map.put(context.readRowHolder().getRowIndex(), data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        titleMap = headMap;
    }

    public Map<Integer, Map<Integer, String>> getDataMap() {
        return this.map;
    }

    public Map<Integer, String> getTitleMap() {
        return this.titleMap;
    }

}
