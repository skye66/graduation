package com.gdut.graduation.dao;

import com.gdut.graduation.pojo.Shipping;
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
 * @Date 2019/3/30 16:59
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ShippingMapperTest {

    @Autowired
    private ShippingMapper shippingMapper;

    @Test
    @Transactional
    public void deleteByShippingIdUserId(){
        int count = shippingMapper.deleteByShippingIdUserId(1,1);
        Assert.assertNotEquals(0,count);
    }
    @Test
    public void updateByShippingIdUserId(){
        Shipping shipping = shippingMapper.selectByShippingIdUserId(1,1);
        shipping.setReceiverName("s");
        int count = shippingMapper.updateByShippingIdUserId(shipping);
        Assert.assertNotEquals(0,count);
    }
    @Test
    public void selectByShippingIdUserId(){
        Shipping shipping = shippingMapper.selectByShippingIdUserId(1,1);
        Assert.assertNotNull(shipping);
    }
    @Test
    public void selectByUserId(){
        List<Shipping> shippingList = shippingMapper.selectByUserId(1);
        Assert.assertNotEquals(0,shippingList.size());
    }
}