package com.cloud.common.utils.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 */
public class DateUtil {
    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    private static String FORMAT_Y_M_D_1 = "yyyy/MM/dd";
    private static String FORMAT_Y_M_D_2 = "yyyy-MM-dd";
    private static String FORMAT_Y_M_D_3 = "yyyyMMdd";
    private static String FORMAT_Y_M_D_4 = "yyyy.MM.dd";
    private static String FORMAT_Y_M_D_5 = "yyyy年MM月dd日 HH:mm";
    private static String FORMAT_Y_M_D_6 ="yyyy-MM";


    private static String FORMAT_Y_M_D_H_M_1 = "yyyy/MM/dd HH:mm";
    private static String FORMAT_Y_M_D_H_M_2 = "yyyy-MM-dd HH:mm";
    private static String FORMAT_Y_M_D_H_M_3 = "yyyyMMddHHmm";

    private static String FORMAT_Y_M_D_H_M_S_1 = "yyyy/MM/dd HH:mm:ss";
    private static String FORMAT_Y_M_D_H_M_S_2 = "yyyy-MM-dd HH:mm:ss";
    private static String FORMAT_Y_M_D_H_M_S_3 = "yyyyMMddHHmmss";


    private static final String[] zodiacArr = {
            "猴", "鸡", "狗", "猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊"
    };

    private static final String[] constellationArr = {
            "水瓶座", "双鱼座", "牡羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
            "天蝎座", "射手座", "魔羯座"
    };

    private static final int[] constellationEdgeDay = {
            20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22
    };

    private static final String[] weekDays = {
            "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"
    };

    private static final Integer[] weekDayNum = {
            7, 1, 2, 3, 4, 5, 6
    };

    private final static long minute = 60 * 1000;// 1分钟

    private final static long hour = 60 * minute;// 1小时

    private final static long day = 24 * hour;// 1天

    private final static long month = 31 * day;// 月

    private final static long year = 12 * month;// 年

    private static final String[] weeks = {
            "周日", "周一", "周二", "周三", "周四", "周五", "周六"
    };


    public static String formatYMD1(Date d) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_Y_M_D_1).format(d);
    }

    public static String formatYMD2(Date d) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_Y_M_D_2).format(d);
    }

    public static String formatYMD3(Date d) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_Y_M_D_3).format(d);
    }

    public static String formatYMD4(Date d) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_Y_M_D_4).format(d);
    }

    public static String formatYMD5(Date d) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_Y_M_D_5).format(d);
    }

    public static String formatYMD6(Date d) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_Y_M_D_6).format(d);
    }

    public static String formatYMDHM1(Date d) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_Y_M_D_H_M_1).format(d);
    }

    public static String formatYMDHM2(Date d) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_Y_M_D_H_M_2).format(d);
    }

    public static String formatYMDHM3(Date d) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_Y_M_D_H_M_3).format(d);
    }

    public static String formatYMDHMS1(Date d) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_Y_M_D_H_M_S_1).format(d);
    }

    public static String formatYMDHMS2(Date d) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_Y_M_D_H_M_S_2).format(d);
    }

    public static String formatYMDHMS3(Date d) {
        if (d == null) {
            return "";
        }
        return new SimpleDateFormat(FORMAT_Y_M_D_H_M_S_3).format(d);
    }

    public static String formatDate(String format, Date date) {
        return new SimpleDateFormat(format).format(date);
    }


    /**
     * Parse date and time like "yyyy-MM-dd HH:mm".
     */
    public static Date parseDateTime1(String dt) {
        try {
            return new SimpleDateFormat(FORMAT_Y_M_D_H_M_1).parse(dt);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Parse date and time like "yyyy-MM-dd HH:mm:ss".
     */
    public static Date parseYMDHMS2(String dt) {
        try {
            return new SimpleDateFormat(FORMAT_Y_M_D_H_M_S_2).parse(dt);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * Parse date like "yyyy-MM-dd".
     */
    public static Date parseDate2(String d) {
        try {
            return new SimpleDateFormat(FORMAT_Y_M_D_2).parse(d);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 返回日期是星期几 文字星期几
     */
    public static String getWeekOfStr(Date dt) {
        int w = getWeekNum(dt);
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 返回日期是星期几 数字
     */
    public static Integer getWeekOfNum(Date dt) {
        int w = getWeekNum(dt);
        if (w < 0) {
            w = 1;
        }
        return weekDayNum[w];
    }

    private static int getWeekNum(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    // -----------------获取指定日期的年份，月份，日份，小时，分，秒，毫秒----------------------------

    /**
     * 获取指定日期的年份
     */
    public static int getYearOfDate(Date p_date) {
        if (null != p_date) {
            Calendar c = Calendar.getInstance();
            c.setTime(p_date);
            return c.get(Calendar.YEAR);
        }
        return -1;
    }

    /**
     * 获取指定日期的月份
     */
    public static int getMonthOfDate(Date p_date) {
        if (null != p_date) {
            Calendar c = Calendar.getInstance();
            c.setTime(p_date);
            return c.get(Calendar.MONTH) + 1;
        }
        return -1;
    }

    /**
     * 获取指定日期的日份
     */
    public static int getDayOfDate(Date p_date) {
        if (null != p_date) {
            Calendar c = Calendar.getInstance();
            c.setTime(p_date);
            return c.get(Calendar.DAY_OF_MONTH);
        }
        return -1;
    }

    /**
     * 获取指定日期的小时
     */
    public static int getHourOfDate(Date p_date) {
        if (null != p_date) {
            Calendar c = Calendar.getInstance();
            c.setTime(p_date);
            return c.get(Calendar.HOUR_OF_DAY);
        }
        return -1;
    }

    /**
     * 获取指定日期的分钟
     */
    public static int getMinuteOfDate(Date p_date) {
        if (null != p_date) {
            Calendar c = Calendar.getInstance();
            c.setTime(p_date);
            return c.get(Calendar.MINUTE);
        }
        return -1;
    }

    /**
     * 获取指定日期的秒钟
     */
    public static int getSecondOfDate(Date p_date) {
        if (null != p_date) {
            Calendar c = Calendar.getInstance();
            c.setTime(p_date);
            return c.get(Calendar.SECOND);
        }
        return -1;
    }

    /**
     * 获取指定日期的毫秒
     */
    public static long getMillisOfDate(Date p_date) {
        if (null != p_date) {
            Calendar c = Calendar.getInstance();
            c.setTime(p_date);
            return c.getTimeInMillis();
        }
        return -1;
    }

    /**
     * 根据日期获取年龄
     */
    public static int getAge(Date birthDay) throws Exception {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                // monthNow==monthBirth
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                } else {
                    // do nothing
                }
            } else {
                // monthNow>monthBirth
                age--;
            }
        } else {
            // monthNow<monthBirth
            // donothing
        }

        return age;
    }

    /**
     * Java 毫秒转换为（天：时）方法
     */
    @SuppressWarnings("hiding")
    public static String formatDayHour(long ms, int validDay) {
        int temp = (int) (ms / (60 * 60 * 1000));
        int day = 0;
        int hour = 0;
        if (temp % 24 == 0) {
            day = (validDay * 24 - temp) / 24;
            hour = 0;
        } else {
            day = (validDay * 24 - temp) / 24;
            if (temp < 24) {
                hour = 24 - temp;
            } else {
                hour = 24 - temp % 24;
            }
        }
        return new StringBuffer().append(day).append("天").append(hour).append("小时").toString();
    }

    /**
     * Java 毫秒转换为（天：时：分：秒）方法
     */
    @SuppressWarnings("hiding")
    public static String formatDayHourMinuteSeconds(long ms) {
        int seconds = (int) (ms / 1000) % 60;
        int minute = (int) (ms / 1000 / 60) % 60;
        int hour = (int) (ms / 1000 / 60 / 60) % 60;
        int day = (int) (ms / 1000 / 60 / 60 / 24) % 24;
        StringBuffer stringBuffer = new StringBuffer();
        if (day > 0) {
            stringBuffer.append(day).append("天");
        }
        if (hour > 0) {
            stringBuffer.append(hour).append("小时");
        }
        if (minute > 0) {
            stringBuffer.append(minute).append("分");
        }
        if (seconds > 0) {
            stringBuffer.append(seconds).append("秒");
        }
        return stringBuffer.toString();
    }

    /**
     * 根据日期获取生肖
     */
    public static String date2Zodica(Calendar time) {
        return zodiacArr[time.get(Calendar.YEAR) % 12];
    }

    /**
     * 根据日期获取星座
     */
    @SuppressWarnings("hiding")
    public static String date2Constellation(Calendar time) {
        int month = time.get(Calendar.MONTH);
        int day = time.get(Calendar.DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return constellationArr[month];
        }
        return constellationArr[11];
    }

    /**
     * 比较传入日期是否已过期(与服务器当前日期比较)
     *
     * @param inputDate 传入日期
     * @return true 未过期(即传入日期大于当前日期)
     * false 已过期(即传入日期小于等于当前日期)
     */
    public static Boolean isOverdate(Date inputDate) {
        Date nowDate = new Date();
        return nowDate.before(inputDate);
    }

    /**
     * 返回一个日期加上几个月后的时间
     *
     * @param inputDate 传入日期
     */
    public static Date getDatePlusMonths(Date inputDate, int monthNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        calendar.add(Calendar.MONTH, monthNum);
        return calendar.getTime();
    }

    /**
     * 返回一个日期加上几年后的时间
     *
     * @param inputDate 传入日期
     */
    public static Date getDatePlusYears(Date inputDate, int yearNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        calendar.add(Calendar.YEAR, yearNum);
        return calendar.getTime();
    }

    /**
     * 返回一个日期加上几天后的日期
     *
     * @param inputDate 传入的日期
     * @param dayNum    增加的天数
     */
    public static Date getDatePlusDays(Date inputDate, int dayNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        calendar.add(Calendar.DAY_OF_WEEK, dayNum);
        return calendar.getTime();
    }

    /**
     * 返回一个日期加上几小时后的日期
     *
     * @param inputDate 传入的日期
     * @param hourNum   增加的小时
     */
    public static Date getDatePlusHour(Date inputDate, int hourNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        calendar.add(Calendar.HOUR_OF_DAY, hourNum);
        return calendar.getTime();
    }

    /**
     * 返回一个日期加上几分钟后的日期
     *
     * @param inputDate 传入的日期
     * @param minuteNum 增加的分钟
     */
    public static Date getDatePlusMinute(Date inputDate, int minuteNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        calendar.add(Calendar.MINUTE, minuteNum);
        return calendar.getTime();
    }

    /**
     * 返回一个日期加上几小时和几分钟后的日期
     *
     * @param inputDate 传入的时间
     * @param hourNum   增加的小时
     * @param minuteNum 增加的分钟
     * @return 增加后的时间
     */
    public static Date getDatePlusHourAndMinute(Date inputDate, int hourNum, int minuteNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(inputDate);
        calendar.add(Calendar.HOUR_OF_DAY, hourNum);
        calendar.add(Calendar.MINUTE, minuteNum);
        return calendar.getTime();
    }

    /**
     * @return 当前中国时区的TIMESTAMP日期
     */
    public static Timestamp getSysTimestamp() {
        final TimeZone zone = TimeZone.getTimeZone("GMT+8");// 获取中国时区
        TimeZone.setDefault(zone);// 设置时区
        return new Timestamp(new Date().getTime());
    }

    public static String timeStampUrl(String url) {
        if (url != null) {
            return url + "?t=" + System.currentTimeMillis();
        }
        return "";
    }

    public static String getYesterDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    public static void main(String[] args) {
        System.out.println(getYesterDate());
    }



    private static Calendar getCalendarDayStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;

    }

    private static Calendar getCalendarDayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
        }
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar;
    }
    // 获得指定日期：开始时间
    public static String getDayStart(Date date) {
        return formatYMDHMS2(getCalendarDayStart(date).getTime());
    }
    // 获得指定日期：结束时间
    public static String getDayEnd(Date date) {
        return formatYMDHMS2(getCalendarDayEnd(date).getTime());
    }

    // 获得当前天：开始时间
    public static String getCurrentDayStart() {
        return formatYMDHMS2(getCalendarDayStart(null).getTime());
    }

    // 获得当前天：结束时间
    public static String getCurrentDayEnd() {
        return formatYMDHMS2(getCalendarDayEnd(null).getTime());
    }

    // 获得当前月：开始的日期
    public static String getCurrentMonthStart() {
        Calendar currentDate = getCalendarDayStart(null);
        currentDate.add(Calendar.MONTH, 0);
        currentDate.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        return formatYMDHMS2(currentDate.getTime());
    }

    // 获得当前月：最后一天的日期
    public static String getCurrentMonthEnd() {
        Calendar currentDate = getCalendarDayEnd(null);
        //获取当前月最后一天
        currentDate.set(Calendar.DAY_OF_MONTH, currentDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        return formatYMDHMS2(currentDate.getTime());
    }

    // 获得当前周- 周一的日期
    public static String getCurrentMonday() {
        int mondayPlus = getMondayPlus();
        Calendar currentDate = getCalendarDayStart(null);
        currentDate.add(GregorianCalendar.DATE, mondayPlus);
        return formatYMDHMS2(currentDate.getTime());
    }

    // 获得当前周- 周日 的日期
    public static String getCurrentSunday() {
        int mondayPlus = getMondayPlus();
        Calendar currentDate = getCalendarDayEnd(null);
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 6);
        return formatYMDHMS2(currentDate.getTime());
    }

    // 得前一周 周一的日期
    public static String getPreMonday() {
        int mondayPlus = getMondayPlus();
        Calendar currentDate = getCalendarDayStart(null);
        currentDate.add(GregorianCalendar.DATE, mondayPlus - 7);
        return formatYMDHMS2(currentDate.getTime());
    }

    // 获得前一周 周日 的日期
    public static String getPreSunday() {
        int mondayPlus = getMondayPlus();
        Calendar currentDate = getCalendarDayEnd(null);
        currentDate.add(GregorianCalendar.DATE, mondayPlus - 1);
        return formatYMDHMS2(currentDate.getTime());
    }

    //获取本年的开始时间
    public static String getCurrentYearStart() {
        Calendar calendar = getCalendarDayStart(null);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DATE, 1);
        return formatYMDHMS2(calendar.getTime());
    }

    //获取本年的结束时间
    public static String getCurrentYearEnd() {
        Calendar calendar = getCalendarDayEnd(null);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        calendar.set(Calendar.DATE, 31);
        return formatYMDHMS2(calendar.getTime());
    }

    private static String getPreMonday(Calendar currentDate) {
        Date monday = currentDate.getTime();
        DateFormat df = DateFormat.getDateInstance();
        String[] mondays = df.format(monday).split("-");
        String month = mondays[1].length() == 1 ? "0" + mondays[1] : mondays[1];
        String day = mondays[2].length() == 1 ? "0" + mondays[2] : mondays[2];
        String preMonday = mondays[0] + "-" + month + "-" + day;
        return preMonday;
    }

    // 获得当前日期与本周一相差的天数
    public static int getMondayPlus() {
        Calendar cd = Calendar.getInstance();
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1) {
            return -6;
        } else {
            return 2 - dayOfWeek;
        }
    }

    /**
     * 获取指定日期的年月日
     *
     * @param date 日期
     */
    public static Date getDateYMD(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_Y_M_D_2);
        String format = simpleDateFormat.format(date);
        try {
            return simpleDateFormat.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断时间是否在指定区间内
     *
     * @param date  需判断的时间
     * @param range 区间 （08:30-20:30）
     * @return
     */
    public static boolean inBetween(Date date, String range) {
        String[] split = range.split("-");
        if (split.length < 2) {
            return false;
        }
        Date startDete = getTimeByHourMinute(date, split[0]);
        Date endDete = getTimeByHourMinute(date, split[1]);
        if (startDete.before(date) && endDete.after(date)) {
            return true;
        }
        return false;
    }

    /**
     * 根据小时和分钟字符串，获取指定日期的具体时间
     *
     * @param date       指定日期
     * @param hourMinute 08:30
     */
    public static Date getTimeByHourMinute(Date date, String hourMinute) {
        Date nowYMD = DateUtil.getDateYMD(date);
        String[] split = hourMinute.split(":");
        int startHour = Integer.parseInt(split[0]);
        int startMinute = Integer.parseInt(split[1]);
        return getDatePlusHourAndMinute(nowYMD, startHour, startMinute);
    }

    /**
     * 当天的结束时间
     *
     * @return
     */
    public static long endOfTodDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        Date date = calendar.getTime();
        return date.getTime();
    }

    /**
     * 获取当前时间距离当天结束的时间
     * <p>
     *
     * @return 秒数
     */
    public static Long endOfTodDayForSecond() {
        long endOfTodDay = endOfTodDay();
        return (endOfTodDay - new Date().getTime()) / 1000;
    }

    /**
     * date2比date1多的天数
     * 两个日期相差的天数
     */
    public static int differentDays(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1 = cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if (year1 != year2) {//同一年
            int timeDistance = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {//闰年
                    timeDistance += 366;
                } else {//不是闰年
                    timeDistance += 365;
                }
            }
            return timeDistance + (day2 - day1);
        } else {//不同年
            return day2 - day1;
        }
    }

    /**
     * 获取指定日期所在周的所有日期列表
     * @param date
     */
    public static List<Date> getWeekDaysByDate(Date date){
        List<Date> list=new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        for(int i=0;i<7;i++){
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek()+i);
            list.add(cal.getTime());
        }
        return list;
    }

    /**
     * 获取某年某月多少天
     * @param date
     * @return
     */
    public static int getDaysByDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int days=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return days;
    }

}
