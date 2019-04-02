package com.gdut.graduation.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 返回给前端的购物车对象
 * @Author Skye
 * @Date 2019/4/1 11:13
 * @Version 1.0
 **/
@Data
public class CartVo {
    List<CartProductVo> cartProductVoList;
    BigDecimal cartTotalPrice;
    boolean allChecked;
    String imageHost;
}
