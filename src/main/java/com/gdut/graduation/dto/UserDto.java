package com.gdut.graduation.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Description 传输的数据对象UserDto
 * @Author Skye
 * @Date 2019/3/24 19:45
 * @Version 1.0
 **/
@Data
public class UserDto {
    /**
     * 用户的id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     *用户手机号
     */
    private String phone;
    /**
     *用户邮箱
     */
    private String email;
    /**
     *用户的角色，0表示普通用户，1表示管理员用户
     */
    private Integer role;
}
