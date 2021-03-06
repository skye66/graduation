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
        Assert.assertEquals(user.getId(),new Integer(1));
    }
    @Test
    public void test(){
        System.out.println(1);
    }

    @Test
    public void updateByPrimaryKeySelective(){
        User user = new User();
        user.setEmail("123456@qq.com");
        user.setId(1007);
        int count = userMapper.updateByPrimaryKeySelective(user);
        Assert.assertNotEquals(0,count);
    }
}