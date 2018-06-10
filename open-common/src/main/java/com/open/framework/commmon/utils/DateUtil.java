/*
 * Copyright 2017 Neusoft All right reserved. This software is the confidential and proprietary information of Neusoft
 * ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with Neusoft.
 */

package com.open.framework.commmon.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 类DateUtils.java的实现描述：
 * 
 */

public class DateUtil extends DateUtils {

    /**
     * @param yyyy-MM-dd
     * @return
     */
    public static final String patternA = "yyyy-MM-dd";

    public static final String patternB = "yyyy年MM月dd日";

    public static final String patternC = "HH:mm:ss";

    public static final String patternD = "yyyy-MM-dd HH:mm:ss";

    /**
     * 格式化日期为yyyy-MM-dd
     * 
     * @param date
     * @return yyyy-MM-dd
     */
    public static String formateDatePatternA(Date date) {
        return dateToString(date, patternA);
    }

    /**
     * 格式化日期为yyyy-MM-dd
     * 
     * @param date
     * @return yyyy年MM月dd日
     */
    public static String formateDatePatternB(Date date) {
        return dateToString(date, patternB);
    }

    /**
     * 格式化日期为yyyy-MM-dd
     * 
     * @param date
     * @return HH:mm:ss
     */
    public static String formateDatePatternC(Date date) {
        return dateToString(date, patternC);
    }

    /**
     * 格式化日期为yyyy-MM-dd
     * 
     * @param date
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formateDatePatternD(Date date) {
        return dateToString(date, patternD);
    }

    /**
     * @param 取当天日期
     * @return
     */
    public static Date getNowDate() {
        return Calendar.getInstance().getTime();
    }
    /**
     * @param 取当天日期的字符串
     * @return
     */
    public static String getNowDateStr() {
        return formateDatePatternD(new Date());
    }

    /**
     * 获取昨天此时的日期
     * @return
     */
    public static Date getYesterdayDate() {
        return addDays(new Date(),-1);
    }
    public static String getYesterdayDateStr(String... pattern) {
        return addDayStr(new Date(),-1,pattern);
    }
    /**
     * 获取明天此时的日期
     * @return
     */
    public static Date getTomorrowDate() {
        return addDays(new Date(),1);
    }
    public static String getTomorrowDateStr(String... pattern) {
        return addDayStr(new Date(),1,pattern);
    }

    /**
     * 获取日期加减天数返回的日期
     * @param date
     * @param i 正数加天数,负数减天数
     * @return String
     */
    public static String addDayStr(Date date,int i,String... pattern){
        Date dateTemp=addDays(date, i);
        if(null!=pattern && pattern.length>0){
            return dateToString(dateTemp,pattern[0]);
        }
        return formateDatePatternD(dateTemp);
    }
    /**
     * @param 将指定日期,以指定pattern格式,输出String值
     * @return
     */
    public static String dateToString(Date date, String pattern) {
        if (date == null) {
            return "";
        } else {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.format(date);
        }
    }

    /**
     * @param 将指定字符型日期转为日期型,,格式为指定的pattern
     * @return
     */
    public static Date stringToDate(String string, String pattern) {
        SimpleDateFormat format = (SimpleDateFormat) DateFormat.getDateInstance();
        format.applyPattern(pattern);
        try {
            return format.parse(string);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * @param 根据传入日期，返回此月有多少天
     * @param date 格式为 201408
     * @return
     */
    public static int getDayOfMonth(String date) {
        int year = Integer.parseInt(date.substring(0, 3));
        int month = Integer.parseInt(date.substring(date.length() - 1, date.length()));

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);// Java月份才0开始算 6代表上一个月7月
        int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
        return dateOfMonth;
    }

    /**
     * 两个日期比较大小,用yyyy-MM-dd HH:mm:ss方式比较
     * @param DATE1
     * @param DATE2
     * @return 1:第一个比第二个大;-1:第一个比第二个小;0:两个相等
     */
    public static int compareDate(Date DATE1, Date DATE2) {
        DateFormat df = new SimpleDateFormat(patternD);
        try {
        	String s_date1 = df.format(DATE1);
          	String s_date2 = df.format(DATE2);
          	
            Date dt1 =df.parse(s_date1);
            Date dt2 = df.parse(s_date2);
            
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     *
     * @param date1
     * @param date2
     * @param type 默认返回天,1代表分,2代表秒
     * @return
     * @throws ParseException
     */
    public static int dayBetween(Date date1, Date date2,int...type) throws ParseException {
        long time1 = date1.getTime();
        long time2 = date2.getTime();
        Integer i=1000 * 3600 * 24;
        if(null!=type && type.length>0){
            if(1==type[0]){
                i=1000 * 3600;
            }else if(2==type[0]){
                i=1000;
            }
        }
        long between_days = (time2 - time1) /i;
        return Integer.parseInt(String.valueOf(between_days));
    }
}
