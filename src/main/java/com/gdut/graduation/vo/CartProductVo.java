package com.gdut.graduation.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 包含了购物车和产品信息的视图对象
 * @Author Skye
 * @Date 2019/4/1 11:13
 * @Version 1.0
 **/
@Data
public class CartProductVo {
    /**
     * 购物车的id
     */
    private Integer id;

    /**
     * 用户的id
     */
    private Integer userId;

    /**
     * 产品的id
     */
    private Integer productId;

    /**
     * 购买产品的数量
     */
    private Integer quantity;

    /**
     * 产品名
     */
    private String productName;
    /**
     * 产品的图标
     */
    private String productIcon;
    /**
     * 产品的价格
     */
    private BigDecimal productPrice;
    /**
     * 产品的状态
     */
    private Integer productStatus;
    /**
     * 产品的总价格，为数量乘金额
     */
    private BigDecimal productTotalPrice;
    /**
     * 产品库存
     */
    private Integer productStock;
    /**
     * 产品是否被选中
     */
    private Integer productChecked;

    /**
     * 限制产品数量，success时，代表库存足够，fail时则将数量更改为最大值
     */
    private String limitQuantity;
}
