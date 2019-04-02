package com.gdut.graduation.util;

import java.math.BigDecimal;

/**
 * @Description 使用BigDecimal的字符串进行数学计算
 * @Author Skye
 * @Date 2019/4/1 21:47
 * @Version 1.0
 **/
public class BigDecimalUtil {
    private BigDecimalUtil(){}
    public static BigDecimal mul(double d1,double d2){
        return new BigDecimal(Double.toString(d1)).multiply(new BigDecimal(Double.toString(d2)));
    }
    public static BigDecimal div(double d1,double d2){
        return new BigDecimal(Double.toString(d1)).divide(new BigDecimal(Double.toString(d2)));
    }
    public static BigDecimal add(double d1,double d2){
        return new BigDecimal(Double.toString(d1)).add(new BigDecimal(Double.toString(d2)));
    }
    public static BigDecimal sub(double d1,double d2){
        return new BigDecimal(Double.toString(d1)).subtract(new BigDecimal(Double.toString(d2)));
    }
}
