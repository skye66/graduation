package com.gdut.graduation.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 前端视图订单产品视图对象
 * @Author Skye
 * @Date 2019/4/2 21:17
 * @Version 1.0
 **/
@Data
public class OrderProductVo {
    /**
     * 订单产品详情
     */
    private List<OrderItemVo> orderItemVoList;
    /**
     * 产品总价格
     */
    private BigDecimal productTotalPrice;
    /**
     * 订单的图片host
     */
    private String imageHost;
}
