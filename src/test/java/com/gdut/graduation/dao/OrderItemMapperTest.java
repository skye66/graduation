package com.gdut.graduation.dao;

import com.gdut.graduation.pojo.OrderItem;
import com.gdut.graduation.serveice.OrderService;
import com.gdut.graduation.serveice.impl.OrderServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Description
 * @Author Skye
 * @Date 2019/4/3 11:19
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderItemMapperTest {

    @Autowired
    private OrderItemMapper orderItemMapper;
    @Test
    public void batchInsert(){
//        orderItemMapper.batchInsert()
    }
    @Test
    public void inset(){
        OrderItem orderItem = new OrderItem();
//        orderItemMapper.insert();
    }
}