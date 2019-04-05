package com.gdut.graduation.util;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description 时间工具类，将时间转换成字符串和将字符串转换成时间
 * @Author Skye
 * @Date 2019/4/3 15:30
 * @Version 1.0
 **/
public class DateTimeUtil {
    private DateTimeUtil(){}
    private final static String FORMATE="yyyy-MM-dd HH:mm:ss";
    public static String datetimeToStr(Date date){
        if (date == null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATE);
        String strDate = dateFormat.format(date);
        return strDate;
    }
    public static Date strToDateTime(String strDate){
        if (StringUtils.isEmpty(strDate)) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATE);
        Date date = null;
        try {
            date = dateFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static void main(String[] args) {
        String str = DateTimeUtil.datetimeToStr(new Date());
        System.out.println(str);
        Date date = DateTimeUtil.strToDateTime(str);
        System.out.println(date.toString());
    }
}
