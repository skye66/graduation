package com.gdut.graduation.aspects;

import com.gdut.graduation.dto.UserDto;
import com.gdut.graduation.enums.ResultEnum;
import com.gdut.graduation.exception.GraduationException;
import common.Const;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Description 登陆授权校验
 * @Author Skye
 * @Date 2019/4/9 16:12
 * @Version 1.0
 **/
@Aspect
@Component
@Slf4j
public class GraduationAuthorizedAspect {
    @Pointcut("execution(public * com.gdut.graduation.controller.*.*(..))" +
            "&& !execution(public * com.gdut.graduation.controller.UserController.login(..))" +
            "&& !execution(public * com.gdut.graduation.controller.UserController.register(..))" +
            "&& !execution(public * com.gdut.graduation.controller.UserController.checkValid(..))")
    public void verifyUser(){

    }
    @Pointcut("execution(public * com.gdut.graduation.controller.portal.*.*(..)) " +
            "&& !execution(public * com.gdut.graduation.controller.portal.UserManageController.*(..)))")
    public void verifyAdmin(){

    }
    @Before("verifyUser()")
    public void userVerify() {
        UserDto userDto = getUserDto();
        if (userDto == null){
            log.error("【用户登陆】未登录");
            throw new GraduationException(ResultEnum.USER_NO_LOGIN);
        }
    }
    @Before("verifyAdmin()")
    public void adminVerify(){
        UserDto userDto = getUserDto();
        if (userDto == null){
            log.error("【用户登陆】管理员未登录");
            throw new GraduationException(ResultEnum.USER_NO_LOGIN);
        }
        if (userDto.getRole()!=Const.Role.ROLE_ADMIN){
            log.error("【用户登陆】不是管理员");
            throw new GraduationException(ResultEnum.USER_NOT_ADMIN);
        }
    }
    private UserDto getUserDto(){
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpSession session = request.getSession();
        return  (UserDto) session.getAttribute(Const.CURRENT_USER);
//        return userDto;
    }
}
