package com.gdut.graduation.serveice.impl;

import com.gdut.graduation.serveice.ProductService;
import com.github.pagehelper.PageInfo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Description
 * @Author Skye
 * @Date 2019/4/14 21:30
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceImplTest {

    @Autowired
    private ProductService productService;
    @Test
    public void setSaleStatus() {
    }

    @Test
    public void searchProductList() {
    }

    @Test
    public void searchProductListByCategoryKeyWord() {
        PageInfo pageInfo =productService.searchProductListByCategoryKeyWord(0,null,1,1000);
            Assert.assertNotEquals(0,pageInfo.getList().size());
    }
}