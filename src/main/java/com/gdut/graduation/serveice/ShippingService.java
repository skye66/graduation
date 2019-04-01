package com.gdut.graduation.serveice;

import com.gdut.graduation.pojo.Shipping;

import java.util.List;

/**
 * @Description
 * @Author Skye
 * @Date 2019/3/31 20:22
 * @Version 1.0
 **/
public interface ShippingService {
    Shipping add(Integer userId,Shipping shipping);
    boolean del(Integer userId,Integer shippingId);
    Shipping update(Integer userId,Shipping shipping);
    Shipping select(Integer userId,Integer shippingId);
    List<Shipping> selectAll(Integer userId);
}
