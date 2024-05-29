package com.cloud.test.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.cloud.test.excel.entity.SimpleEntity;
import com.cloud.test.excel.listener.AnalysisSimpleListener;
import com.cloud.test.excel.listener.SimpleEntityListener;

import java.util.List;
import java.util.Map;

public class ReadTest {

    public static void main(String[] args) {

        String fileName = "/Users/cloud/study/study_project/testFile/test.xlsx";
        test1(fileName);

    }

    /**
     * 根据文件的表头匹配字段名
     * @param fileName
     */
    public static void test1(String fileName) {
        SimpleEntityListener listener = new SimpleEntityListener();
        EasyExcel.read(fileName, SimpleEntity.class, listener).sheet().doRead();

        List<SimpleEntity> dataList = listener.getDataList();
        System.out.println(JSONObject.toJSONString(dataList));
    }

    // AnalysisSimpleListener
    public static void test2(String fileName) {
        AnalysisSimpleListener listener = new AnalysisSimpleListener();
        EasyExcel.read(fileName, listener).sheet().doRead();
        Map<Integer, Map<Integer, String>> dataMap = listener.getDataMap();
        Map<Integer, String> titleMap = listener.getTitleMap();

        System.out.println(JSONObject.toJSONString(dataMap));
        System.out.println(JSONObject.toJSONString(titleMap));
    }

}
