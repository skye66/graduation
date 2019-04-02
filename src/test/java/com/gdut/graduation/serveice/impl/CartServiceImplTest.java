package com.gdut.graduation.serveice.impl;

import com.gdut.graduation.pojo.Cart;
import com.gdut.graduation.serveice.CartService;
import com.gdut.graduation.vo.CartVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Description
 * @Author Skye
 * @Date 2019/4/2 10:13
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class CartServiceImplTest {

    @Autowired
    private CartService cartService;
    @Test
    public void create() {
        CartVo cartVo = cartService.create(1,2,45);
        Assert.assertNotNull(cartVo);
    }

    @Test
    public void update() {
        CartVo cartVo = cartService.update(1,2,80);
        Assert.assertNotNull(cartVo);
    }

    @Test
    @Rollback
    public void delete() {
        List list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        boolean res = cartService.delete(1,list);
        Assert.assertTrue(res);
    }

    @Test
    public void list() {
        CartVo vo = cartService.list(1);
        Assert.assertNotNull(vo);
    }

    @Test
    public void selectOrUnSelect() {
        CartVo cartVo = cartService.selectOrUnSelect(1,2,1);
        Assert.assertNotNull(cartVo);
    }

    @Test
    public void getCartProductCount() {
        int count = cartService.getCartProductCount(1);
        Assert.assertNotEquals(0,count);
    }
}