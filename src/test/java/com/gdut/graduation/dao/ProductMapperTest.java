package com.gdut.graduation.dao;

import com.gdut.graduation.pojo.Product;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @Description 测试ProductMapper增删改查
 * @Author Skye
 * @Date 2019/3/27 21:11
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;


    @Test
    public void insert(){
        Product product = new Product();
        product.setName("软件工程");
        product.setPrice(new BigDecimal(20));
        product.setStock(100);
        product.setCategoryId(20);
        product.setIcon("http://xxx.xxx");
        product.setStatus(1);
        int id =productMapper.insert(product);
        log.info("【产品测试】id:{}",id);
        Assert.assertNotEquals(0,id);

    }
    @Test
    public void selectByPrimaryKey(){
        Product product = productMapper.selectByPrimaryKey(1);
        Assert.assertNotNull(product);
    }
    @Test
    public void selectAll(){
        List<Product> productList = productMapper.selectAll();
        Assert.assertNotEquals(0,productList.size());

    }

    @Test
    public void updateByPrimary(){
        Product product = productMapper.selectByPrimaryKey(1);
        product.setStatus(2);

        int count = productMapper.updateByPrimaryKey(product);
        Assert.assertNotEquals(0,count);
    }

    @Test
    public void deleteByPrimaryKey(){
        int count = productMapper.deleteByPrimaryKey(1);
        Assert.assertNotEquals(0,count);

    }
    @Test
    public void selectByStatus(){
        List<Product> productList = productMapper.selectByStatus(0);
        Assert.assertNotEquals(0,productList.size());
    }
    @Test
    public void selectByProductNameId(){
        List<Product> productList = productMapper.selectByProductNameId("%spring%",2);
        Assert.assertNotEquals(0,productList.size());
    }
    @Test
    public void selectByProductNameCategoryIds(){
        List<Integer> list = new ArrayList<>();
        list.add(20);
        list.add(21);
        list.add(25);
        list.add(22);
        List<Product> productList = productMapper.selectByProductNameCategoryIds(null,list );
        Assert.assertNotEquals(0,productList);
    }
    @Test
    public void selectByProductIds(){
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        List<Product> productList = productMapper.selectByProductIds(list);
        Assert.assertNotEquals(0,productList.size());
    }
}