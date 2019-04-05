package com.gdut.graduation.util;

import java.util.Random;
import java.util.UUID;

/**
 * @Description 随机生成键值
 * @Author Skye
 * @Date 2019/3/27 21:16
 * @Version 1.0
 **/
public class KeyUtil {
    private KeyUtil(){}
    public static synchronized String getRanomKey(){
        long curr = System.currentTimeMillis();
        String str = curr+ Math.random()*9999999+"";
        return str;
    }
    public static synchronized String getUniqueOrderNo(){
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        for (int i=0;i<10;i++) {
            String key = KeyUtil.getUniqueOrderNo();
            System.out.println(key);
        }
    }
}
