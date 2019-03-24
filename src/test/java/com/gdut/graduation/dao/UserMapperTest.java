package com.gdut.graduation.dao;

import com.gdut.graduation.pojo.User;
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
 * @Date 2019/3/24 10:18
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectByPrimaryKey(){
        User user = userMapper.selectByPrimaryKey(1);
        Assert.assertNotEquals(user.getId(),new Integer(1));
    }
    @Test
    public void test(){
        System.out.println(1);
    }
}