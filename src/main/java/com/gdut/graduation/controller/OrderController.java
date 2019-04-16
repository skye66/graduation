package com.gdut.graduation.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.gdut.graduation.dto.UserDto;
import com.gdut.graduation.pojo.User;
import com.gdut.graduation.serveice.OrderService;
import com.gdut.graduation.vo.OrderProductVo;
import com.gdut.graduation.vo.OrderVo;
import com.gdut.graduation.vo.ResultVo;
import com.github.pagehelper.PageInfo;
import common.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
            return ResultVo.createBySuccess(true);
        }else return ResultVo.createByError("该用户并没有该订单,查询无效");
    }

    @RequestMapping("pay")
    @ResponseBody
    public ResultVo pay(HttpSession session, String orderNo, HttpServletRequest request){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);

//        String path = request.getSession().getServletContext().getRealPath("upload");
        //该path为图片二维码的存储路径
        return orderService.pay(orderNo, userDto.getId(), null);
    }
    @RequestMapping("alipay_callback")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){
        Map requestParams = request.getParameterMap();
        Map<String, String> params = new HashMap<>();
        for (Iterator iterator = requestParams.keySet().iterator(); iterator.hasNext();){
            String name = (String) iterator.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i ++){
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        log.info("支付宝回调，sign:{}, trade_status:{},参数: {}", params.get("sign"), params.get("trade_status"),params.toString());
        //非常重要，验证回调的重要性，是不是支付宝发的，并且呢还要避免重复通知。

        try {
            boolean alipayRSACheckV2 = AlipaySignature.rsaCheckV2(params, Configs.getPublicKey(), "urt-8", Configs.getSignType());
            if(!alipayRSACheckV2){
                return ResultVo.createByError("非法请求，验证不通过，再恶意请求我就报警找网警了");
            }
        } catch (AlipayApiException e) {
            log.info("支付宝回调异常", e);
        }

        //todo 验证各种数据

        ResultVo res  = orderService.aliCallBack(params);
        if (res.getStatus() == 0){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }
}
