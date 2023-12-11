package com.cloud.common.utils.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LocalDateUtil {


	public static final String FORMAT_TEMPLATE_1 = "yyyyMMddHHmmss";
	public static final String FORMAT_TEMPLATE_2 = "yyyyMMddHHmmssSSS";
	public static final String FORMAT_TEMPLATE_3 = "yyyy-MM-dd";
	public static final String FORMAT_TEMPLATE_4 = "yyyy-MM";

    /* -------------------------获取指定月份的第一天和最后一天----------------------------------------------*/
    /**
     * @param date YYYY-MM
     * @return 获取指定月份第一天 YYYY-MM-DD
     */
    public static String getFirstDay(String date) {
        return date + "-01";
    }

    /**
     * @param date YYYY-MM
     * @return 获取指定月份最后一天 YYYY-MM-DD
     */
    public static String getLastDay(String date) {
        LocalDate localDate = LocalDate.parse(date + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate lastDay = localDate.with(TemporalAdjusters.lastDayOfMonth());
        return lastDay.toString();
    }

    /* ------------------------------------------两个时间点包含多个年月日------------------------------------- */
    /**
     * 获取两个月份之间的所有月份（包含start和end）
     * isRatio：是否是同比，true的话，年减一
     */
    public static List<String> getBetweenMonth(String startDate, String endDate) {
        List<String> list = new ArrayList<>();
        LocalDate start = LocalDate.parse(startDate + "-01");
        LocalDate end = LocalDate.parse(endDate + "-01");
        long distance = ChronoUnit.MONTHS.between(start, end);
        Stream.iterate(start, d -> d.plusMonths(1)).limit(distance + 1).forEach(f -> {
            list.add(f.toString().substring(0, 7));
        });

        return list;
    }

    /**
     * 两个时间所有年份
     */
    public static List<String> getBetweenYear(String startDate, String endDate) {
        List<String> list = new ArrayList<>();
        LocalDate start = LocalDate.parse(startDate + "-01-01");
        LocalDate end = LocalDate.parse(endDate + "-12-31");
        long distance = ChronoUnit.YEARS.between(start, end);
        Stream.iterate(start, d -> d.plusYears(1)).limit(distance + 1).forEach(f -> {
            list.add(f.toString().substring(0, 4));
        });

        return list;
    }

    /**
     * 两个时间所有天
     */
    public static List<String> getBetweenDay(String startDate, String endDate, boolean isBasis) {
        List<String> list = new ArrayList<>();
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        long distance = ChronoUnit.DAYS.between(start, end);
        if(isBasis) {
            start = start.minusYears(1);
        }
        Stream.iterate(start, d -> d.plusDays(1)).limit(distance + 1).forEach(f -> {
            list.add(f.toString());
        });

        return list;
    }

    /* ----------------------------------获取同比月和环比月-------------------------------------------- */
    /**
     * 环比：上个月 
     */
    public static String getLastMonth(String startDate) {
        LocalDate localDate = LocalDate.parse(startDate + "-01");
        String lastMonth = localDate.minusMonths(1).toString().substring(0, 7);
        return lastMonth;
    }

    /**
     * 同比：去年这个月 
     */
    public static String getLastYear(String startDate) {
        LocalDate localDate = LocalDate.parse(startDate + "-01");
        String lastMonth = localDate.minusYears(1).toString().substring(0, 7);
        return lastMonth;
    }


}
