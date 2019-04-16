package com.gdut.graduation.controller;

import com.gdut.graduation.dto.UserDto;
import com.gdut.graduation.enums.ResultEnum;
import com.gdut.graduation.exception.GraduationException;
import com.gdut.graduation.pojo.Shipping;
import com.gdut.graduation.pojo.User;
import com.gdut.graduation.serveice.ShippingService;
import com.gdut.graduation.vo.ResultVo;
import common.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @Description 收货地址控制器
 * @Author Skye
 * @Date 2019/3/31 21:21
 * @Version 1.0
 **/
@RestController
@RequestMapping("/shipping")
public class ShippingController {
    @Autowired
    private ShippingService shippingService;
    @GetMapping("/list")
    public ResultVo list(HttpSession session, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                         @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
        UserDto user = (UserDto) session.getAttribute(Const.CURRENT_USER);
        if (user==null) throw new GraduationException(ResultEnum.USER_NO_LOGIN);
        Integer userId = user.getId();
        return ResultVo.createBySuccess(shippingService.selectAll(userId,pageNum,pageSize));
    }
    @GetMapping("/information")
    public ResultVo information(HttpSession session,Integer shippingId){
        UserDto user = (UserDto) session.getAttribute(Const.CURRENT_USER);
        if (user==null) throw new GraduationException(ResultEnum.USER_NO_LOGIN);
        return ResultVo.createBySuccess(shippingService.select(user.getId(),shippingId));
    }
    @PostMapping("/create")
    public ResultVo create(HttpSession session, @Valid Shipping shipping, BindingResult bindingResult){
        UserDto user = (UserDto) session.getAttribute(Const.CURRENT_USER);
        if (user==null) throw new GraduationException(ResultEnum.USER_NO_LOGIN);
        if (bindingResult.hasErrors()){
            throw new GraduationException(ResultEnum.ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }
        if (StringUtils.isEmpty(shipping)){
            throw new GraduationException(ResultEnum.PARAM_ERROR);
        }
        shipping.setUserId(user.getId());
        return ResultVo.createBySuccess(shippingService.add(user.getId(),shipping));
    }
    @PostMapping("/update")
    public ResultVo update(HttpSession session, @Valid Shipping shipping,BindingResult bindingResult){
        UserDto user = (UserDto) session.getAttribute(Const.CURRENT_USER);
        if (user==null) throw new GraduationException(ResultEnum.USER_NO_LOGIN);
        if (bindingResult.hasErrors()){
            throw new GraduationException(ResultEnum.ERROR.getCode(),bindingResult.getFieldError().getDefaultMessage());
        }

        if (StringUtils.isEmpty(shipping)){
            throw new GraduationException(ResultEnum.PARAM_ERROR);
        }
        return ResultVo.createBySuccess(shippingService.update(user.getId(),shipping));
    }
    @GetMapping("/delete")
    public ResultVo delete(HttpSession session, Integer shippingId){
        UserDto user = (UserDto) session.getAttribute(Const.CURRENT_USER);
        if (user==null) throw new GraduationException(ResultEnum.USER_NO_LOGIN);
        if (shippingId == null){
            throw new GraduationException(ResultEnum.PARAM_ERROR);
        }
        return ResultVo.createBySuccess(shippingService.del(user.getId(),shippingId));
    }
}
