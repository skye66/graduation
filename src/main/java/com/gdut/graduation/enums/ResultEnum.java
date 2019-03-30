package com.gdut.graduation.enums;

import lombok.Getter;

/**
 * @Description 返回消息状态码
 * @Author Skye
 * @Date 2019/3/24 20:28
 * @Version 1.0
 **/
@Getter
public enum  ResultEnum {
    SUCCESS(0,"成功"),
    ERROR(1,"错误"),
    PARAM_ERROR(2,"参数错误"),
    /**
     * 用户错误10xx
     */
    USER_NOT_EXISTS(1001,"用户不存在"),
    USER_UPDATE_ERROR(1002,"更新用户信息失败"),
    USER_USERNAME_PASSWORD(1003,"用户名或者密码错误"),
    USER_EXISTS(1004,"用户已存在"),
    USER_CREATE_ERROR(1005,"用户添加失败"),
    USER_NO_LOGIN(1006,"当前用户未登录"),

    /**
     * 类目的错误信息20xx
     */
    CATEGORY_ID_EMPTY(2001,"类目id为空"),
    CATEGORY_UPDATE_ERROR(2002,"类目更新失败"),
    CATEGORY_EMPTY(2003,"类目不存在"),
    CATEGORY_CREATE_ERROR(2004,"添加失败"),

    /**
     * 产品类的提示信息30xx
     */
    PRODUCT_PARAM_ERROR(3001,"参数错误"),
    PRODUCT_NOT_EXISTS(3002,"产品不存在"),
    PRODUCT_UPDATE_CREATE_ERROR(3003,"新增或更新失败"),


    ;

    private ResultEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
    private int code;
    private String msg;
}
