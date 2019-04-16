package com.gdut.graduation.serveice;

import com.gdut.graduation.vo.OrderProductVo;
import com.gdut.graduation.vo.OrderVo;
import com.gdut.graduation.vo.ResultVo;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * @Description 订单服务（订单详情管理、订单支付管理等）
 * @Author Skye
 * @Date 2019/4/2 21:07
 * @Version 1.0
 **/
public interface OrderService {

    ResultVo pay(String orderNo, Integer userId, String path);

    ResultVo aliCallBack(Map<String, String> params);
    boolean queryOrderPayStatus(Integer userId,String orderNo);

    OrderVo createOrder(Integer userId, Integer shippingId);

    boolean cancel(Integer userId,String orderNo);

    OrderProductVo getOrderCartProduct(Integer userId);

    OrderVo getOrderDetail(Integer userId,String orderNo);

    PageInfo<List<OrderVo>> getOrderList(Integer userId,int pageNum,int pageSize);

    //backend

    PageInfo<List<OrderVo>> manageList(int pageNum,int pageSize);

    OrderVo manageOrderDetail(String orderNo);

    PageInfo<List<OrderVo>> manageSearch(String orderNo,int pageNum,int pageSize);

    String manageSendGoods(String orderNo);

}
