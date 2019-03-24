package com.gdut.graduation.serveice.impl;

import com.gdut.graduation.dto.UserDto;
import com.gdut.graduation.pojo.User;
import com.gdut.graduation.serveice.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Description 测试UserService
 * @Author Skye
 * @Date 2019/3/24 21:28
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @Test
    public void selectById() {
        UserDto user = userService.selectById(1);
        Assert.assertNotNull(user);
    }

    @Test
    public void updateUserInformation() {
        User user = new User();
        user.setId(1);
        user.setEmail("2233@qq.com");

        UserDto userDto = userService.updateUserInformation(user);
        Assert.assertNotNull(userDto);
    }

    @Test
    public void selectByUsernameAndPassword() {
        UserDto userDto = userService.selectByUsernameAndPassword("admin","admin");
        Assert.assertNotNull(userDto);
    }

    @Test
    public void createUser() {
        User user = new User();
        user.setEmail("test@email.com");
        user.setId(1000);
        user.setUsername("test");
        user.setPassword("test");
        user.setRole(1);
        UserDto userDto = userService.createUser(user);
        Assert.assertNotNull(userDto);

    }
}