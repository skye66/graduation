package com.gdut.graduation.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 订单详情视图对象
 * @Author Skye
 * @Date 2019/4/2 21:18
 * @Version 1.0
 **/
@Data
public class OrderItemVo {
    private String orderNo;

    private Integer productId;

    private String productName;

    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;
}
