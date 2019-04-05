package com.gdut.graduation.controller.portal;

import com.gdut.graduation.dto.UserDto;
import com.gdut.graduation.enums.ResultEnum;
import com.gdut.graduation.exception.GraduationException;
import com.gdut.graduation.serveice.UserService;
import com.gdut.graduation.vo.ResultVo;
import common.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Description 管理员控制器
 * @Author Skye
 * @Date 2019/4/5 10:49
 * @Version 1.0
 **/
@RestController
@RequestMapping("/manage/user")
public class UserManageController {
    @Autowired
    private UserService userService;
    @PostMapping("/login")
    public ResultVo login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session){

        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            throw new GraduationException(ResultEnum.PARAM_ERROR);
        }
        UserDto userDto = userService.selectByUsernameAndPassword(username, password);
        if (userDto.getRole()!=Const.Role.ROLE_ADMIN){
            throw new GraduationException(ResultEnum.USER_NOT_ADMIN);
        }
        session.setAttribute(Const.CURRENT_USER, userDto);
        return ResultVo.createBySuccess(userDto);
    }
}
