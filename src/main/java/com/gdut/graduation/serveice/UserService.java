package com.gdut.graduation.serveice;

import com.gdut.graduation.dto.UserDto;
import com.gdut.graduation.pojo.User;

/**
 * @Description UserService接口，注册的时候使用jdk的动态注入，以及约定的通信契约。
 * @Author Skye
 * @Date 2019/3/24 19:40
 * @Version 1.0
 **/
public interface UserService {
    /**
     * 通过用户的id获取用户的信息，可以查询登陆用户的信息
     * @return
     */
    UserDto selectById(int id);
    /**
     *更新用户的密码、基本信息等
     */
    UserDto updateUserInformation(User user);

    /**
     * 通过用户名和密码查找用户信息
     * @return
     */
    UserDto selectByUsernameAndPassword(String username,String password);

    /**
     * 添加、注册新用户
     * @param user
     * @return
     */
    UserDto createUser(User user);
}
