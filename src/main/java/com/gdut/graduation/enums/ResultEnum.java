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
    USER_CREATE_ERROR(1005,"用户添加失败")


    ;

    private ResultEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
    private int code;
    private String msg;
}
