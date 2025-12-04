package com.cloud.test.demo.stream;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;
import com.cloud.test.demo.stream.entity.Demo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class GroupStream {

    public static void main(String[] args) throws Exception {
        System.out.println(LocalDate.now());
        System.out.println(LocalDateTime.now().toString());

    }



    public static void read2() {
        ExcelReader reader = ExcelUtil.getReader("/Users/cloud/Downloads/新疆省间现货数据1.xlsx", 0);
        List<List<Object>> read = reader.read(1);
        for (List<Object> obj : read) {
            System.out.println("insert into ads_grid_pomc_t_yxzx_out_jxqk_df(pdate, eqtype, eqname, begintime, endtime, reptype, voltage, updatatime) values ('"
                    + obj.get(0) + "', '" + obj.get(1) + "', '" + obj.get(2) + "', '" + obj.get(3) + "', '"
                    + obj.get(4) + "', '" + obj.get(5) + "', '" + obj.get(6) + "', '" + obj.get(7) + "');") ;
        }
        System.out.println(JSONObject.toJSONString(read));
    }

    public static void read1() {
        ExcelReader reader = ExcelUtil.getReader("/Users/cloud/Downloads/新疆省间现货数据1.xlsx", 1);
        List<List<Object>> read = reader.read(1);
        for (List<Object> obj : read) {
            System.out.println("insert into ads_grid_pomc_t_yxzx_out_kzsnyfycd_df(pdate, periodid, pvalue) values ("
                    + obj.get(0) + "," + obj.get(1) + "," + obj.get(2) + ");") ;
        }
        System.out.println(JSONObject.toJSONString(read));
    }

    public static void test1() {
        BigDecimal b = new BigDecimal("23.1272");
        System.out.println(b.setScale(2, RoundingMode.DOWN));
    }

    // 使用POI读取"D:\test\test.sql"文件的内容



    private static String getNowTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String recTime = simpleDateFormat.format(calendar.getTime());
        return recTime;
    }

    /**
     * 分组
     * @throws Exception
     */
    public static void test() throws Exception {
        List<Demo> list = getList();

        //两次分组 (分组字段不能为null)
        Map<String, Map<String, List<Demo>>> collect = list.stream()
                .filter(x -> StringUtils.isNotBlank(x.getTime()) && StringUtils.isNotBlank(x.getName()))
                .collect(Collectors.groupingBy(Demo::getTime, Collectors.groupingBy(Demo::getName))
                );
        System.out.println("分组两次：" + JSONObject.toJSONString(collect));

        //分组统计次数
        Map<String, Long> countCol = list.stream().collect(Collectors.groupingBy(
                Demo::getTime, Collectors.counting()
        ));
        System.out.println("分组统计次数：" + JSONObject.toJSONString(countCol));

        // 分组后取某个字段的最大值对应的记录
        Map<String, Demo> collect1 = list.stream().collect(Collectors.groupingBy(
                Demo::getTime, Collectors.collectingAndThen(
//                        Collectors.reducing((t1, t2) -> t1.getId() > t2.getId() ? t1 : t2),
//                        Collectors.reducing((t1, t2) -> t1.getDate().after(t2.getDate()) ? t1 : t2),
                        Collectors.reducing((t1, t2) -> t1.getLocalTime().isAfter(t2.getLocalTime()) ? t1 : t2),
                        Optional::get)
        ));
        System.out.println("分组后取列表中的最值：：" + JSONObject.toJSONString(collect1));

        // 分组后排序(根据ID降序)
        Map<String, List<Demo>> collect3 = list.stream().collect(
                Collectors.groupingBy(Demo::getTime,
                        HashMap::new,
                        Collectors.collectingAndThen(Collectors.toList(),
                                x -> x.stream().sorted(Comparator.comparing(Demo::getId).reversed()).collect(Collectors.toList()))
                ));
        System.out.println("分组后排序：" + JSONObject.toJSONString(collect3));

    }

    /*
        分区：key为布尔值
     */
    public static void partition() throws Exception {
        List<Demo> list = getList();
        Map<Boolean, Long> collect = list.stream().collect(Collectors.partitioningBy(x -> "a".equals(x.getName()), Collectors.counting()));
        System.out.println("partition result: " + JSONObject.toJSONString(collect));
    }

    public static void partition2(List<Demo> list) throws Exception {
        // 根据time字段分组后，取每组中localTime最大的那一条
        Map<String, Demo> collect = list.stream().collect(Collectors.groupingBy(
                Demo::getTime, Collectors.collectingAndThen(
                        Collectors.reducing((t1, t2) -> t1.getLocalTime().isAfter(t2.getLocalTime()) ? t1 : t2),
                        Optional::get)
        ));

        // 根据time字段分组，每组根据date排序
        Map<String, List<Demo>> collect1 = list.stream().collect(
                Collectors.groupingBy(Demo::getTime,
                        HashMap::new,
                        Collectors.collectingAndThen(Collectors.toList(),
                                x -> x.stream().sorted(Comparator.comparing(Demo::getDate)).collect(Collectors.toList()))
                ));


        //筛选出时间LocalTime在2023-02-01 00:00:00 之后的数据
        List<Demo> collect2 = list.stream()
                .filter(x -> x.getLocalTime().isAfter(LocalDateTime.of(2023, 2, 1, 0, 0, 0)))
                .collect(Collectors.toList());

    }

    public static List<Demo> getList() throws Exception {
        List<Demo> list = Lists.newArrayList();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        list.add(new Demo(1l, 1, "a", "2023-06", sdf.parse("2023-06-01 00:00:00"),
                LocalDateTime.of(2023, 6, 1, 0, 0, 0)));

        list.add(new Demo(2l, 1, "b", "2023-04", sdf.parse("2023-04-01 00:00:00"),
                LocalDateTime.of(2023, 4, 1, 0, 0, 0)));

        list.add(new Demo(3l, 1, "c", "2023-06", sdf.parse("2023-06-01 10:00:00"),
                LocalDateTime.of(2023, 6, 1, 10, 0, 0)));

        list.add(new Demo(4l, 1, "d", "2023-05", sdf.parse("2023-05-01 00:00:00"),
                LocalDateTime.of(2023, 5, 1, 0, 0, 0)));

        list.add(new Demo(5l, 1, "e", "2023-07", sdf.parse("2023-07-01 00:00:00"),
                LocalDateTime.of(2023, 7, 1, 0, 0, 0)));

        return list;
    }

}
