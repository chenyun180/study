package com.cloud.test.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.merge.LoopMergeStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSONObject;
import com.cloud.test.excel.entity.*;
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.*;

public class CommonWriteTest {

    public static void main(String[] args) {

        LocalDate now = LocalDate.parse("2024-07-01");
        System.out.println(now.getYear());

        String fileName = "/Users/cloud/study/study_project/testFile/write3.xlsx";
//        write1(fileName);
//        writeMerge(fileName);
//        writeExcludeFiled(fileName);
//        complexHeadWrite(fileName);
//        converterWrite(fileName);

//        dynamicHeadWrite(fileName);

    }


    /**
     * web中的导出
     * @param response
     * @throws IOException
     */
    @GetMapping("download")
    public void download(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), SimpleEntity.class).sheet("模板").doWrite(data());
    }

    /**
     * 动态表头
     * head()
     * @param fileName
     */
    public static void dynamicHeadWrite(String fileName) {
        EasyExcel.write(fileName, SimpleEntity.class).head(head1()).sheet("模板1").doWrite(data());
    }

    private static List<List<String>> head1() {
        List<List<String>> list = new ArrayList<>();
        List<String> head0 = new ArrayList<>();
        head0.add("类型");
        head0.add("字符串");
        List<String> head1 = new ArrayList<>();
        head1.add("类型");
        head1.add("数字");
        List<String> head2 = new ArrayList<>();
        head2.add("日期" + System.currentTimeMillis());
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

    private static List<List<String>> head() {
        List<List<String>> list = new ArrayList<>();
        List<String> head0 = new ArrayList<>();
        head0.add("字符串" + System.currentTimeMillis());
        List<String> head1 = new ArrayList<>();
        head1.add("数字" + System.currentTimeMillis());
        List<String> head2 = new ArrayList<>();
        head2.add("日期" + System.currentTimeMillis());
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

    /**
     * 单元格合并（注解形式 简单的）
     * @param fileName
     */
    public static void writeMerge(String fileName) {
        EasyExcel.write(fileName, MergeDataEntity.class).sheet("模板1").doWrite(data());
    }

    /**
     * 单元格合并（使用LoopMergeStrategy）
     * @param fileName
     */
    public static void writeMergeStrategy(String fileName) {
        LoopMergeStrategy loopMergeStrategy = new LoopMergeStrategy(2, 0);
        EasyExcel.write(fileName, SimpleEntity.class).sheet("模板1").doWrite(data());
    }


    /**
     * 导出排除某些字段
     * @param fileName
     */
    public static void writeExcludeFiled(String fileName) {

        // 忽略date字段
        Set<String> excludeColumnFiledNames = new HashSet<>();
        excludeColumnFiledNames.add("date");
        EasyExcel.write(fileName, WriteEntity.class).excludeColumnFieldNames(excludeColumnFiledNames)
                .sheet("模板1").doWrite(data());
    }

    /**
     * 正常写入
     * 自动列宽：registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())，根据标题的字数自动扩展列宽
     * @param fileName
     */
    public static void write1(String fileName) {
        EasyExcel.write(fileName, WriteEntity.class).registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("模板1").doWrite(data());
    }

    /**
     * 复杂头写入：标题简单的单元格合并（要是很复杂，直接定义模板）
     * @param fileName
     */
    public static void complexHeadWrite(String fileName) {
        EasyExcel.write(fileName, ComplexWriteEntity.class).sheet("模板111").doWrite(data());
    }

    /**
     * 字段转换
     * @param fileName
     */
    public static void converterWrite(String fileName) {
        EasyExcel.write(fileName, ConverterDataEntity.class).sheet("模板111").doWrite(data());
    }

    private static List<WriteEntity> data() {
        List<WriteEntity> list = Lists.newArrayList();
        for (int i = 0; i < 10; i++) {
            WriteEntity data = new WriteEntity();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

}
