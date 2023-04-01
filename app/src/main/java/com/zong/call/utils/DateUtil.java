package com.zong.call.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * author: zzj
 * time: 2020/11/11
 * deprecated :
 */

public class DateUtil {


    /*
     * 将时间戳转换为时间
     */
    public static String getCurrentHourAndMinute() {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
        long lt = new Long(System.currentTimeMillis());
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDateTime(long s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(s);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;// +1是因为返回来的值并不是代表月份，而是对应于Calendar.MAY常数的值，
        // Calendar在月份上的常数值从Calendar.JANUARY开始是0，到Calendar.DECEMBER的11
    }

    /**
     * 获取当前的时间为该月的第几天
     *
     * @return
     */
    public static int getCurrentMonthDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }


    public static int getWeekDay(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, --month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取当前的年月
     *
     * @return
     */
    public static String getYearMonth(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        return calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月";
    }

    /**
     * 获取当前的年月
     *
     * @return
     */
    public static String getTimeDetail(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        return calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH) + 1) + "月";
    }

    /**
     * 获取当前时间为该天的多少点
     *
     * @return
     */
    public static int getHour() {
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        // Calendar calendar = Calendar.getInstance();
        // System.out.println(calendar.get(Calendar.HOUR_OF_DAY_OF_DAY)); // 24小时制
        // System.out.println(calendar.get(Calendar.HOUR_OF_DAY)); // 12小时制
    }

    /**
     * 获取当前的分钟时间
     *
     * @return
     */
    public static int getMinute() {
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    /**
     * 通过获得年份和月份确定该月的日期分布
     *
     * @param year
     * @param month
     * @return
     */
    public static int[][] getMonthNumFromDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);// -1是因为赋的值并不是代表月份，而是对应于Calendar.MAY常数的值，


        int days[][] = new int[6][7];// 存储该月的日期分布

        int firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);// 获得该月的第一天位于周几（需要注意的是，一周的第一天为周日，值为1）

        int monthDaysNum = getMonthDaysNum(year, month);// 获得该月的天数
        // 获得上个月的天数
        int lastMonthDaysNum = getLastMonthDaysNum(year, month);

        // 填充本月的日期
        int dayNum = 1;
        int lastDayNum = 1;
        for (int i = 0; i < days.length; i++) {
            for (int j = 0; j < days[i].length; j++) {
                if (i == 0 && j < firstDayOfWeek - 1) {// 填充上个月的剩余部分
                    days[i][j] = lastMonthDaysNum - firstDayOfWeek + 2 + j;
                } else if (dayNum <= monthDaysNum) {// 填充本月
                    days[i][j] = dayNum++;
                } else {// 填充下个月的未来部分
                    days[i][j] = lastDayNum++;
                }
            }
        }

        return days;

    }

    public static List<Map<String, String>> getWeekNumFromDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -weekDay);
        List<Map<String, String>> week = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DATE, 1);
            Map<String, String> map = new HashMap<>();
            map.put("month", calendar.get(Calendar.MONTH) + 1 + "月");
            map.put("day", calendar.get(Calendar.DATE) + "日");
            map.put("state", "0");
            map.put("focus", "0");
            week.add(map);
        }
        return week;
    }

    public static List<Map<String, String>> getWeekNumFromDate(int year, int month, int day, int focus) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.add(Calendar.DATE, -weekDay);
        List<Map<String, String>> week = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            calendar.add(Calendar.DATE, 1);
            Map<String, String> map = new HashMap<>();
            map.put("month", calendar.get(Calendar.MONTH) + 1 + "月");
            map.put("day", calendar.get(Calendar.DATE) + "日");
            map.put("state", "0");
            map.put("focus", "0");
            if (calendar.get(Calendar.DATE) == day) {
                map.put("focus", focus + "");
            }
            week.add(map);
        }
        return week;
    }

    /**
     * 根据年数以及月份数获得上个月的天数
     *
     * @param year
     * @param month
     * @return
     */
    public static int getLastMonthDaysNum(int year, int month) {

        int lastMonthDaysNum = 0;

        if (month == 1) {
            lastMonthDaysNum = getMonthDaysNum(year - 1, 12);
        } else {
            lastMonthDaysNum = getMonthDaysNum(year, month - 1);
        }
        return lastMonthDaysNum;

    }

    /**
     * 根据年数以及月份数获得该月的天数
     *
     * @param year
     * @param month
     * @return 若返回为负一，这说明输入的年数和月数不符合规格
     */
    public static int getMonthDaysNum(int year, int month) {

        if (year < 0 || month <= 0 || month > 12) {// 对于年份与月份进行简单判断
            return -1;
        }

        int[] array = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};// 一年中，每个月份的天数

        if (month != 2) {
            return array[month - 1];
        } else {
            if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {// 闰年判断
                return 29;
            } else {
                return 28;
            }
        }

    }

    /**
     * 获取两个日期之间的时间间隔
     *
     * @return
     */
    public static String getGapCount(int fromYear, int fromMonth, int fromDay, int fromHour, int fromMinute,
                                     int endYear, int endMonth, int endDay, int endHour, int endMinute) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(Calendar.YEAR, fromYear);
        fromCalendar.set(Calendar.MONTH, --fromMonth);
        fromCalendar.set(Calendar.DAY_OF_MONTH, fromDay);
        fromCalendar.set(Calendar.HOUR_OF_DAY, fromHour);
        fromCalendar.set(Calendar.MINUTE, fromMinute);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(Calendar.YEAR, endYear);
        toCalendar.set(Calendar.MONTH, --endMonth);
        toCalendar.set(Calendar.DAY_OF_MONTH, endDay);
        toCalendar.set(Calendar.HOUR_OF_DAY, endHour);
        toCalendar.set(Calendar.MINUTE, endMinute);

        String str = "";

        int minutes = (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60));

        int day = minutes / (60 * 24);
        minutes = minutes - day * 24 * 60;
        if (day < 10) {
            str += "0" + day + "天";
        } else {
            str += day + "天";
        }

        int hour = minutes / 60;
        minutes = minutes - hour * 60;

        if (hour < 10) {
            str += "0" + hour + "时";
        } else {
            str += hour + "时";
        }

        if (minutes < 10) {
            str += "0" + minutes + "分";
        } else {
            str += minutes + "分";
        }

        return str;
    }

    public static int getGapCount2(int fromYear, int fromMonth, int fromDay, int fromHour, int fromMinute,
                                   int endYear, int endMonth, int endDay, int endHour, int endMinute) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(Calendar.YEAR, fromYear);
        fromCalendar.set(Calendar.MONTH, --fromMonth);
        fromCalendar.set(Calendar.DAY_OF_MONTH, fromDay);
        fromCalendar.set(Calendar.HOUR_OF_DAY, fromHour);
        fromCalendar.set(Calendar.MINUTE, fromMinute);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(Calendar.YEAR, endYear);
        toCalendar.set(Calendar.MONTH, --endMonth);
        toCalendar.set(Calendar.DAY_OF_MONTH, endDay);
        toCalendar.set(Calendar.HOUR_OF_DAY, endHour);
        toCalendar.set(Calendar.MINUTE, endMinute);

        int minutes = (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60));

        return minutes;
    }

    public static int getGapCount3(int fromYear, int fromMonth, int fromDay, int endYear, int endMonth, int endDay) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(Calendar.YEAR, fromYear);
        fromCalendar.set(Calendar.MONTH, --fromMonth);
        fromCalendar.set(Calendar.DAY_OF_MONTH, fromDay);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(Calendar.YEAR, endYear);
        toCalendar.set(Calendar.MONTH, --endMonth);
        toCalendar.set(Calendar.DAY_OF_MONTH, endDay);

        int days = (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (24 * 60 * 60 * 1000));

        return days;
    }

    public static double getGapCount4(int fromYear, int fromMonth, int fromDay, int fromHour, int fromMinute,
                                      int endYear, int endMonth, int endDay, int endHour, int endMinute) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.set(Calendar.YEAR, fromYear);
        fromCalendar.set(Calendar.MONTH, --fromMonth);
        fromCalendar.set(Calendar.DAY_OF_MONTH, fromDay);
        fromCalendar.set(Calendar.HOUR_OF_DAY, fromHour);
        fromCalendar.set(Calendar.MINUTE, fromMinute);
        fromCalendar.set(Calendar.SECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.set(Calendar.YEAR, endYear);
        toCalendar.set(Calendar.MONTH, --endMonth);
        toCalendar.set(Calendar.DAY_OF_MONTH, endDay);
        toCalendar.set(Calendar.HOUR_OF_DAY, endHour);
        toCalendar.set(Calendar.MINUTE, endMinute);
        toCalendar.set(Calendar.SECOND, 0);

        int minutes = (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60));

        return minutes / 60.0;
    }

    public static long getTime(int year, int month, int day, int hour, int minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, --month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        return calendar.getTime().getTime();
    }

    public static long getTime(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, --month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTime().getTime();
    }

    public static long getTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime().getTime() / 100000;
    }

    public static Date stringToDate(String time) {
        Log.e("dsds", time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(time);
            Log.e("dsds", date.getYear() + "," + date.getMonth() + "," + date.getDay());
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s) {
        String res;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String getCurrentDate() {
        String res;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd");
        long lt = new Long(System.currentTimeMillis());
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /*
     * 将时间戳转换为时间
     */

    public static String getCurrentYYDate() {
        String res;
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        long lt = new Long(System.currentTimeMillis());
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    private static final String TAG = "DateUtil";

    public static String timeDiff(long dateBefore) {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        //由于字符串本身没有时区的概念，因此 2013-1-31 22:17:14就是指GMT（UTC）时间【ps：所有字符串都看做是GMT时间】
//        String nowDate = dateTransformBetweenTimeZone(new Date(), formatter,
//                TimeZone.getDefault(), TimeZone.getTimeZone("GMT+8"));//将系统时间转为北京时间
//        String beforeDate = dateTransformBetweenTimeZone(new Date(dateBefore), formatter,
//                TimeZone.getDefault(), TimeZone.getTimeZone("GMT+8"));//将系统时间转为北京时间
//
//        Log.i(TAG, "timeDiff: nowDate:" + nowDate);
//        Log.i(TAG, "timeDiff: beforeDate:" + beforeDate);

        try {
//            Date d1 = formatter.parse(nowDate);
////            Date d2 = formatter.parse(formatter.format(new Date(dateBefore)));
//            Date d2 = formatter.parse(dateBefore);

//            long diff = d1.getTime() - d2.getTime();// 这样得到的差值是微秒级别

            long diff = System.currentTimeMillis() - dateBefore;
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24))
                    / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60))
                    / (1000 * 60);


            if (days >= 1) {
                return days + "天前";
            } else {
                if (hours >= 1) {
                    return hours + "小时前";
                } else {
                    return minutes + "分钟前";
                }
            }
        } catch (Exception e) {
            return "";
        }

    }

    public static String payTimeLeft(long time) {
        long minutes = time / (1000 * 60);
        long seconds = (time - 1000 * 60 * minutes) / 1000;
        return minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }

    public static long timeLeft(String dateLate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //由于字符串本身没有时区的概念，因此 2013-1-31 22:17:14就是指GMT（UTC）时间【ps：所有字符串都看做是GMT时间】
        String nowDate = dateTransformBetweenTimeZone(new Date(), formatter,
                TimeZone.getDefault(), TimeZone.getTimeZone("GMT+8"));//将系统时间转为北京时间

        try {
            Date d1 = formatter.parse(nowDate);
            Date d2 = formatter.parse(dateLate);

            long diff = (d1 == null || d2 == null) ? 0 : d2.getTime() - d1.getTime();// 这样得到的差值是微秒级别
            return diff;

        } catch (Exception e) {
            return 0;
        }
    }

    public static String getNowChinaTime() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

    public static int getWeekDay() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static int getChinaHour() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        return calendar.get(Calendar.HOUR_OF_DAY);
    }


    public static String dateTransformBetweenTimeZone(Date sourceDate, DateFormat formatter,
                                                      TimeZone sourceTimeZone, TimeZone targetTimeZone) {
        Long targetTime = sourceDate.getTime() - sourceTimeZone.getRawOffset() + targetTimeZone.getRawOffset();
        return formatter.format(new Date(targetTime));
    }


    /**
     * 在Activity中，限制快速点击
     *
     * @Override public boolean dispatchTouchEvent(MotionEvent ev) {
     * if (ev.getAction() == MotionEvent.ACTION_DOWN) {
     * if (Util.isFastDoubleClick()) {
     * return true;
     * }
     * }
     * return super.dispatchTouchEvent(ev);
     * }
     */
    private static long lastClickTime = System.currentTimeMillis();

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        return timeD <= 1000;
    }


    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        Log.e(null, result);
        return result;
    }

    /**
     * 获取未来 第 future 天的日期
     *
     * @param future
     * @return
     */
    public static String getFutureDate(int future) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + future);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        String result = format.format(today);
        Log.e(null, result);
        return result;
    }


    private static String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    /**
     * 获取未来 第 future 天的星期
     *
     * @param future
     * @return
     */
    public static String getFutureDayWeek(int future) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"));
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + future);
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        return weeks[i - 1];
    }

}
