package com.gdut.graduation.converter;

import com.gdut.graduation.pojo.Shipping;
import com.gdut.graduation.vo.ShippingVo;
import org.springframework.beans.BeanUtils;

/**
 * @Description
 * @Author Skye
 * @Date 2019/4/3 15:49
 * @Version 1.0
 **/
public class Shipping2ShippingVo {
    private Shipping2ShippingVo(){}
    public static ShippingVo convert(Shipping shipping){
        ShippingVo shippingVo=new ShippingVo();
        BeanUtils.copyProperties(shipping,shippingVo);
        return shippingVo;
    }
}
