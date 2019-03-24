package com.gdut.graduation.serveice.impl;

import com.gdut.graduation.converter.User2UserDto;
import com.gdut.graduation.dao.UserMapper;
import com.gdut.graduation.dto.UserDto;
import com.gdut.graduation.enums.ResultEnum;
import com.gdut.graduation.exception.GraduationException;
import com.gdut.graduation.pojo.User;
import com.gdut.graduation.serveice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author Skye
 * @Date 2019/3/24 19:58
 * @Version 1.0
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 查找用户，查找失败则抛出异常
     * @param id 用户的id
     * @return 异常或者用户的信息（当查找成功时）
     */
    @Override
    public UserDto selectById(int id) {

        User user = userMapper.selectByPrimaryKey(id);
        if (user != null) {
            return User2UserDto.convertToDto(user);
        }
        throw new GraduationException(ResultEnum.USER_NOT_EXISTS);
    }

    /**
     * 更新用户的信息，该User必须是数据库中存在的并且控制层需要将用户的id更改为当前以登陆用户的id
     * @param user
     * @return
     */
    @Override
    public UserDto updateUserInformation(User user) {
        User update = userMapper.selectByPrimaryKey(user.getId());
        if (update != null) {
            int count = userMapper.updateByPrimaryKey(update);
            if (count>0) return User2UserDto.convertToDto(update);
        }
        throw  new GraduationException(ResultEnum.USER_UPDATE_ERROR);
    }

    /**
     * 通过用户名和密码获取用户的信息
     * @return
     */
    @Override
    public UserDto selectByUsernameAndPassword(String username,String password) {
        int count = userMapper.selectByUsername(username);
        if (count==0) throw new GraduationException(ResultEnum.USER_NOT_EXISTS);
        User user = userMapper.selectByUsernameAndPassword(username,password);
        if (user!=null) return User2UserDto.convertToDto(user);
        throw new GraduationException(ResultEnum.USER_USERNAME_PASSWORD);
    }

    @Override
    public UserDto createUser(User user) {
        //todo 密码的md5值加密
        int count =userMapper.selectByUsername(user.getUsername());
        if (count!=0) throw new GraduationException(ResultEnum.USER_EXISTS);
        int res = userMapper.insert(user);
        if (res == 0) throw new GraduationException(ResultEnum.USER_CREATE_ERROR);
        return User2UserDto.convertToDto(user);
    }
}
