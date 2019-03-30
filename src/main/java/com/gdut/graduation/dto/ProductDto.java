package com.gdut.graduation.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 产品的传输对象
 * @Author Skye
 * @Date 2019/3/27 21:47
 * @Version 1.0
 **/
@Data
public class ProductDto {

    /**
     * 产品id
     */
    private Integer id;
    /**
     * 产品名
     */
    private String name;
    /**
     * 产品类目id
     */
    private Integer categoryId;
    /**
     * 产品价格
     */
    private BigDecimal price;
    /**
     * 产品库存
     */
    private Integer stock;
    /**
     * 产品图标
     */
    private String icon;
    /**
     * 产品详情
     */
    private String detail;
    /**
     * 产品状态
     */
    private Integer status;
}
