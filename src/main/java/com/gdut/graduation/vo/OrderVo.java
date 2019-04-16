package com.gdut.graduation.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author Skye
 * @Date 2019/4/2 21:13
 * @Version 1.0
 **/
@Data
public class OrderVo {
    /**
     * Order pojo对象中的属性
     */
    private String orderNo;
    private BigDecimal payment;
    private Integer paymentType;
    private String paymentTypeDesc;
    private BigDecimal postage;
    private Integer status;
    private String statusDesc;
    private String paymentTime;
    private String sendTime;
    private String endTime;
    private String closeTime;
    private String createTime;


    //订单明细
    private List<OrderItemVo> orderItemVoList;
    private String imageHost;
    private Integer shippingId;
    private String receiverName;
    //具体的收获地址
    private ShippingVo shippingVo;
}
