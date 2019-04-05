package com.gdut.graduation.controller;

import com.gdut.graduation.dto.UserDto;
import com.gdut.graduation.serveice.OrderService;
import com.gdut.graduation.vo.OrderProductVo;
import com.gdut.graduation.vo.OrderVo;
import com.gdut.graduation.vo.ResultVo;
import com.github.pagehelper.PageInfo;
import common.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Description 用户订单管理
 * @Author Skye
 * @Date 2019/4/4 9:12
 * @Version 1.0
 **/
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/create")
    public ResultVo create(HttpSession session,@RequestParam("shippingId") Integer shippingId){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        OrderVo orderVo = orderService.createOrder(userDto.getId(),shippingId);
        return ResultVo.createBySuccess(orderVo);
    }
    @GetMapping("/cancel")
    public ResultVo cancel(HttpSession session, @RequestParam(value = "orderNo") String orderNo){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        boolean res = orderService.cancel(userDto.getId(),orderNo);
        if (res){
            return ResultVo.createBySuccess("取消成功");
        }else return ResultVo.createByError("取消失败");
    }
    @GetMapping("/get_order_cart_product")
    public ResultVo getOrderCartProduct(HttpSession session){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        OrderProductVo orderProductVo = orderService.getOrderCartProduct(userDto.getId());
        return ResultVo.createBySuccess(orderProductVo);
    }
    @GetMapping("/detail")
    public ResultVo detail(HttpSession session,@RequestParam(value = "orderNo")String orderNo){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        OrderVo orderVo = orderService.getOrderDetail(userDto.getId(),orderNo);
        return ResultVo.createBySuccess(orderVo);
    }
    @GetMapping("/list")
    public ResultVo list(HttpSession session,
                         @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                         @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        PageInfo<List<OrderVo>> pageInfo = orderService.getOrderList(userDto.getId(),pageNum,pageSize);
        return ResultVo.createBySuccess(pageInfo);
    }
    @GetMapping("/query_order_pay_status")
    public ResultVo queryOrderPayStatus(HttpSession session,@RequestParam(value = "orderNo")String orderNo){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        boolean res = orderService.queryOrderPayStatus(userDto.getId(),orderNo);
        if (res){
            return ResultVo.createBySuccess("支付成功");
        }else return ResultVo.createByError("未支付");
    }
}
