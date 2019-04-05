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
    USER_NOT_ADMIN(1007,"不是管理员"),
    USER_EMAIL_EXISTS(1008,"邮件已存在"),
    USER_USERNAME_EXISTS(1009,"用户名已存在"),
    USER_CHECK_TYPE_ERROR(1010,"校验类型错误"),
    USER_CHECK_SUCCESS(1011,"校验通过"),

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
    PRODUCT_NOT_ON_SALE(3004,"产品不是在售状态"),
    PRODUCT_STOCK_NOT_ENOUGH(3005,"产品库存不足"),

    /**
     * 收获地址的提示信息40xx
     */
    SHIPPING_INSERT_ERROR(4001,"新建收货信息失败"),
    SHIPPING_DEL_ERROR(4002,"收货地址删除失败"),
    SHIPPING_UPDATE_ERROR(4003,"收货地址更新错误"),
    SHIPPING_SELECT_ERROR(4004,"不存在收获信息"),

    /**
     * 购物车提示信息50xx
     */
    CART_ADD_ERROR(5001,"创建购物车失败"),
    CART_UPDATE_ERROR(5002,"更新购物车失败"),
    CART_DELETE_ERROR(5003,"删除购物车失败"),
    CART_PARAM_ERROR(5004,"购物车参数错误"),
    CART_NOT_EXISTS(5005,"购物车不存在"),

    /**
     * 订单提示信息60xx
     */
    ORDER_NOT_EXISTS(6001,"订单不存在"),
    ORDER_CREATE_ERROR(6002,"订单创建失败"),
    ORDER_STATUS_UPDATE_ERROR(6003,"订单已支付或已取消"),


    ;

    private ResultEnum(int code,String msg){
        this.code = code;
        this.msg = msg;
    }
    private int code;
    private String msg;
}
