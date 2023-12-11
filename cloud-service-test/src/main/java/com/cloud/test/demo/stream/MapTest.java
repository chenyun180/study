package com.cloud.test.demo.stream;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MapTest {

    // 计算日期

    public static void main(String[] args) {
        createFilePath();

    }

    private static void createFilePath() {
        File file = new File("/Users/cloud/Downloads/11");
        if(!file.exists()) {
            file.mkdirs();
        }
    }

    private static String betweenTime(List<String> timeList, String[] split) {
//        LocalTime start = LocalTime.parse(split[0] + ":00");
        LocalTime start = LocalTime.parse("24:00".equals(split[0]) ? "23:59" : split[0] + ":00");
        LocalTime end = LocalTime.parse("24:00".equals(split[1]) ? "23:59" : split[1] + ":00");
        for (String s : timeList) {
            String[] split1 = s.split("-");
            if(isBetween(split1, start, end)) {
                return s;
            }
        }
        return null;
    }

    private static boolean isBetween(String[] s, LocalTime start, LocalTime end) {
        for (String s1 : s) {
//            LocalTime time1 = LocalTime.parse(s1 + ":00");
            LocalTime time1 = LocalTime.parse("24:00".equals(s1) ? "23:59" : s1 + ":00");
            if((time1.isBefore(end) && time1.isAfter(start)) || time1.compareTo(start) == 0 || time1.compareTo(end) == 0) {
                return true;
            }
        }
        return false;
    }
}
