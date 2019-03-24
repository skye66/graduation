package com.gdut.graduation.converter;

import com.gdut.graduation.dto.UserDto;
import com.gdut.graduation.pojo.User;
import org.springframework.beans.BeanUtils;

/**
 * @Description 将User对象转换成UserDto对象
 * @Author Skye
 * @Date 2019/3/24 20:00
 * @Version 1.0
 **/
public class User2UserDto {
    private User2UserDto(){}
    public static UserDto convertToDto(User user){
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }
}
