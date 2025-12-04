package com.cloud.test.excel.hutool;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.cloud.test.excel.entity.SimpleEntity;

import java.util.List;

public class HutoolReadTest {

    public static void main(String[] args) {
        System.out.println((String) null);
        String fileName = "/Users/cloud/study/study_project/testFile/test.xlsx";
//        readAsBean(fileName);
//        readAsList(fileName);
    }

    /**
     * 推荐使用这个。如果数据量非常大的话，使用EasyExcel
     * 按照单元格的顺序，读为List
     * read()支持file、文件名、输入流
     * @param fileName
     */
    public static void readAsList(String fileName) {
        ExcelReader reader = ExcelUtil.getReader(fileName);
        List<List<Object>> read = reader.read();
        System.out.println(JSONObject.toJSONString(read));
    }

    /**
     * 不推荐：Excel数据读成Bean
     * 这种方式字段名必须和表头一样，而表头一般是中文
     * @param fileName
     */
    public static void readAsBean(String fileName) {
        ExcelReader reader = ExcelUtil.getReader(fileName);
        List<SimpleEntity> list = reader.readAll(SimpleEntity.class);
        System.out.println(JSONObject.toJSONString(list));
    }

}
