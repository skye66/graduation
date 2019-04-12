package com.gdut.graduation.serveice.impl;

import com.gdut.graduation.pojo.Shipping;
import com.gdut.graduation.serveice.ShippingService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description 测试收获地址服务
 * @Author Skye
 * @Date 2019/3/31 20:50
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ShippingServiceImplTest {

    @Autowired
    private ShippingService shippingService;
    @Test
    public void add() {
        int userId=2;
        Shipping shipping = new Shipping();
        shipping.setUserId(userId);
        shipping.setReceiverName("黄家豪");
        shipping.setReceiverPhone("18212344321");
        shipping.setReceiverMobile("020-00001010");
        shipping.setReceiverProvince("广东省");
        shipping.setReceiverCity("揭阳市");
        shipping.setReceiverDistrict("惠来县");
        shipping.setReceiverAddress("xx村xx路");
        shipping.setReceiverZip("510520");
        Shipping res = shippingService.add(userId,shipping);
        Assert.assertNotNull(res);
    }

    @Test
    @Transactional
    public void del() {
        boolean res = shippingService.del(1,1);
        Assert.assertTrue(res);
    }

    @Test
    public void update() {
        Shipping shipping = shippingService.select(1,1);
        shipping.setReceiverName(shipping.getReceiverName()+"_mod");

        Shipping res = shippingService.update(shipping.getUserId(),shipping);
        Assert.assertNotNull(res);
    }

    @Test
    public void select() {
        Shipping shipping = shippingService.select(1,1);
        Assert.assertNotNull(shipping);
    }

    @Test
    public void selectAll() {
        List<Shipping> shippingList = shippingService.selectAll(1);
        Assert.assertNotEquals(0,shippingList.size());
    }
}