/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.nis.galaxy.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 *
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取距当前5分30s前的时间
     *
     * @return
     */
    public static String getBeforeTimeOf5min30s() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.MINUTE, -5);
        gc.add(Calendar.SECOND, -30);
        return DateUtils.formatDateTime(gc.getTime());
        //return "2018-07-01 00:00:00";
    }

    /**
     * 获取距当前1h30s前的时间
     *
     * @return
     */
    public static String getBeforeTimeOf1h30s() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.HOUR, -1);
        gc.add(Calendar.SECOND, -30);
        return DateUtils.formatDateTime(gc.getTime());
        //return "2018-07-01 00:00:00";
    }

    /**
     * 获取距当前24h30s前的时间
     *
     * @return
     */
    public static String getBeforeTimeOf24h30s() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.DATE, -1);
        gc.add(Calendar.SECOND, -30);
        return DateUtils.formatDateTime(gc.getTime());
        //return "2018-07-01 00:00:00";
    }

    /**
     * 距time 5min之后
     *
     * @param time
     * @return
     */
    public static String getAfter5min(String time) {
        Date date = DateUtils.parseDate(time);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.MINUTE, 5);
        return DateUtils.formatDateTime(gc.getTime());
        //return "2018-12-01 00:00:00";
    }

    /**
     * 获取距离time时间minute分钟的时间,之前需加符号-、之后为正+
     *
     * @param time
     * @param minute
     * @return
     */
    public static String getBeforeNminOfTime(String time, int minute) {
        Date date = DateUtils.parseDate(time);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.MINUTE, minute);
        return DateUtils.formatDateTime(gc.getTime());
    }

    /**
     * 距time 24h之后
     *
     * @param time
     * @return
     */
    public static String getAfter24h(String time) {
        Date date = DateUtils.parseDate(time);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.DATE, 1);
        return DateUtils.formatDateTime(gc.getTime());
        //return "2018-12-01 00:00:00";
    }

    /**
     * 距time 1小时之后的时间
     *
     * @param time
     * @return
     */
    public static String getAfter1h(String time) {
        Date date = DateUtils.parseDate(time);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(Calendar.HOUR, 1);
        return DateUtils.formatDateTime(gc.getTime());
        //return "2018-12-01 00:00:00";
    }

 //   //当前时间的上一个5min的整时间
 //   public static String getTimePointNmin(String time){
 //       getTime()
//
 //       return null;
 //   }



    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     *
     * @param l
     */
    public static String getDate(long l) {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     * "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
     * "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     *
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (24 * 60 * 60 * 1000);
    }

    /**
     * 获取过去的小时
     *
     * @param date
     * @return
     */
    public static long pastHour(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 60 * 1000);
    }

    /**
     * 获取过去的分钟
     *
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = System.currentTimeMillis() - date.getTime();
        return t / (60 * 1000);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     *
     * @param timeMillis
     * @return
     */
    public static String formatDateTime(long timeMillis) {
        long day = timeMillis / (24 * 60 * 60 * 1000);
        long hour = (timeMillis / (60 * 60 * 1000) - day * 24);
        long min = ((timeMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (timeMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long sss = (timeMillis - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000 - min * 60 * 1000 - s * 1000);
        return (day > 0 ? day + "," : "") + hour + ":" + min + ":" + s + "." + sss;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * @param args
     * @throws ParseException
     */
    public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
    }
}
