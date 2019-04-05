package com.gdut.graduation.serveice.impl;

import com.gdut.graduation.pojo.Order;
import com.gdut.graduation.serveice.OrderService;
import com.gdut.graduation.vo.OrderProductVo;
import com.gdut.graduation.vo.OrderVo;
import com.github.pagehelper.PageInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Description
 * @Author Skye
 * @Date 2019/4/3 20:42
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;
    private final static String orderNo = "947f28e3-0575-46ff-a704-6dd1cc690723";
    @Test
    public void queryOrderPayStatus() {
        boolean pay = orderService.queryOrderPayStatus(1,orderNo);
        System.out.println(pay);
    }

    @Test
//    @Transactional
    public void createOrder() {

        OrderVo orderVo = orderService.createOrder(1,16);
        Assert.assertNotNull(orderVo);
    }

    @Test
    public void cancel() {
        boolean res = orderService.cancel(1,orderNo);
        Assert.assertTrue(res);
    }

    @Test
    public void getOrderCartProduct() {
        OrderProductVo orderProductVo = orderService.getOrderCartProduct(1);
        Assert.assertNotNull(orderProductVo);
    }

    @Test
    public void getOrderDetail() {
        OrderVo orderVo = orderService.getOrderDetail(1,orderNo);
        Assert.assertNotNull(orderVo);
    }

    @Test
    public void getOrderList() {
        PageInfo<List<OrderVo>> pageInfo = orderService.getOrderList(1,1,10);
        Assert.assertNotNull(pageInfo.getList());
    }

    @Test
    public void manageList() {
        PageInfo<List<OrderVo>> pageInfo = orderService.manageList(1,10);
        Assert.assertNotNull(pageInfo.getList());
    }

    @Test
    public void manageOrderDetail() {
        OrderVo orderVo = orderService.manageOrderDetail(orderNo);
        Assert.assertNotNull(orderVo);
    }

    @Test
    public void manageSearch() {
        PageInfo pageInfo= orderService.manageSearch(orderNo,1,10);
        Assert.assertNotNull(pageInfo.getList());
    }

    @Test
    public void manageSendGoods() {
        String res = orderService.manageSendGoods(orderNo);
        System.out.printf(res);
    }
}