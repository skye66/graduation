package com.gdut.graduation.controller;

import com.gdut.graduation.dto.UserDto;
import com.gdut.graduation.enums.ResultEnum;
import com.gdut.graduation.exception.GraduationException;
import com.gdut.graduation.pojo.User;
import com.gdut.graduation.serveice.UserService;
import com.gdut.graduation.vo.ResultVo;
import common.Const;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Description 用户控制器，实现用户的登陆、注册等
 * @Author Skye
 * @Date 2019/3/26 8:34
 * @Version 1.0
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public ResultVo login(@Param("username") String username, @Param("password") String password, HttpSession session){

        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            throw new GraduationException(ResultEnum.PARAM_ERROR);
        }

        UserDto userDto = userService.selectByUsernameAndPassword(username, password);
        session.setAttribute(Const.CURRENT_USER, userDto);
        return ResultVo.createBySuccess(userDto);
    }
    @PostMapping("/register")
    public ResultVo register(User user){
        if (StringUtils.isEmpty(user)||StringUtils.isEmpty(user.getUsername())||StringUtils.isEmpty(user.getPassword())){
            throw new GraduationException(ResultEnum.PARAM_ERROR);
        }
        UserDto userDto = userService.createUser(user);
        return ResultVo.createBySuccess(userDto);
    }
    @GetMapping("/logout")
    public ResultVo logout(HttpSession session){
        if (session.getAttribute(Const.CURRENT_USER)==null){
            return ResultVo.createByError(ResultEnum.USER_NO_LOGIN);
        }
        session.setAttribute(Const.CURRENT_USER,null);
        return ResultVo.createBySuccess();
    }
    @GetMapping("/update")
    public ResultVo update(User user,HttpSession session){
        if (StringUtils.isEmpty(user)||StringUtils.isEmpty(user.getUsername())||StringUtils.isEmpty(user.getPassword())){
            throw new GraduationException(ResultEnum.PARAM_ERROR);
        }
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        if (userDto == null) throw new GraduationException(ResultEnum.USER_NO_LOGIN);
        user.setUsername(userDto.getUsername());
        UserDto res = userService.updateUserInformation(user);
        return ResultVo.createBySuccess(res);
    }

    @GetMapping("/get_information")
    public ResultVo getInformation(HttpSession session){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        UserDto res = userService.selectById(userDto.getId());
        return ResultVo.createBySuccess(res);
    }

}
