package com.gdut.graduation.dao;

import com.gdut.graduation.pojo.Cart;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Description 测试Cart的dao层
 * @Author Skye
 * @Date 2019/4/1 16:58
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class CartMapperTest {

    @Autowired
    private CartMapper cartMapper;

//    private final int userId=

    @Test
    public void selectByUserIdCartId(){
        Cart cart = cartMapper.selectByUserIdProductId(1,1);
        Assert.assertNotNull(cart);
    }
    @Test
    public void selectByUserId(){
        List<Cart> cartList = cartMapper.selectByUserId(1);
        Assert.assertNotEquals(0,cartList.size());
    }
    @Test
    public void selectCheckedCartByUserId(){
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(1);
        Assert.assertNotEquals(0,cartList.size());
    }
    @Test
    public void selectUnCheckedCartByUserId(){
        List<Cart> cartList=cartMapper.selectUnCheckedCartByUserId(1);
        Assert.assertNotEquals(0,cartList.size());
    }
    @Test
    public void selectCartProductCount(){
        int count = cartMapper.selectCartProductCount(1);
        Assert.assertNotEquals(0,count);
    }
    @Test
    public void CheckedOrUnCheckedUpdate(){
        cartMapper.CheckedOrUnCheckedUpdate(1,1,0);
    }
    @Test
    public void deleteByUserIdProductIds(){
        List list = new ArrayList();
        list.add(10);
        list.add(20);
        list.add(30);
        int count = cartMapper.deleteByUserIdProductIds(1,list);
        Assert.assertNotEquals(0,count);
    }
}