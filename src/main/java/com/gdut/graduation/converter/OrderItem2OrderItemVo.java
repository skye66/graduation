package com.gdut.graduation.converter;

import com.gdut.graduation.pojo.OrderItem;
import com.gdut.graduation.vo.OrderItemVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 订单详情转换成订单视图详情
 * @Author Skye
 * @Date 2019/4/3 16:22
 * @Version 1.0
 **/
public class OrderItem2OrderItemVo {
    private OrderItem2OrderItemVo(){}
    public static OrderItemVo converter(OrderItem orderItem){
        OrderItemVo orderItemVo = new OrderItemVo();
        BeanUtils.copyProperties(orderItem,orderItemVo);
        return orderItemVo;
    }
    public static List<OrderItemVo> converter(List<OrderItem> orderItemList){
        List<OrderItemVo> orderItemVoList = new ArrayList<>();
        for (OrderItem orderItem:orderItemList){
            orderItemVoList.add(converter(orderItem));
        }
        return orderItemVoList;
    }
}
