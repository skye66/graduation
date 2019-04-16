package com.gdut.graduation.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.gdut.graduation.dto.UserDto;
import com.gdut.graduation.enums.ResultEnum;
import com.gdut.graduation.exception.GraduationException;
import com.gdut.graduation.pojo.User;
import com.gdut.graduation.serveice.CartService;
import com.gdut.graduation.vo.CartVo;
import com.gdut.graduation.vo.ResultVo;
import common.Const;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.sun.xml.internal.ws.api.message.Packet.State.ServerResponse;

/**
 * @Description 购物车控制器
 * @Author Skye
 * @Date 2019/4/2 10:39
 * @Version 1.0
 **/
@RestController
@RequestMapping("/cart")
@Slf4j
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/list")
    public ResultVo list(HttpSession session){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        CartVo cartVo = cartService.list(userDto.getId());
        return ResultVo.createBySuccess(cartVo);
    }
    @GetMapping("/update")
    public ResultVo update(HttpSession session, @RequestParam("productId") Integer productId, @RequestParam("count") Integer quantity){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        if (productId==null||quantity==null||userDto==null){
            log.error("【购物车控制器】参数错误");
            throw new GraduationException(ResultEnum.PARAM_ERROR);
        }
        CartVo cartVo = cartService.update(userDto.getId(),productId,quantity);
        return ResultVo.createBySuccess(cartVo);
    }
    @GetMapping("/delete_products")
    public ResultVo delete(HttpSession session,@Param("productIds") String productIds){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        String[] ids = productIds.split(",");
        List<Integer> idList = new ArrayList<>();
        for (String id:ids){
            idList.add(Integer.valueOf(id));
        }
        boolean res = cartService.delete(userDto.getId(),idList);
        if (res){
            return ResultVo.createBySuccess(cartService.list(userDto.getId()));
        }
        else return ResultVo.createByError();
    }
    @GetMapping("/add")
    public ResultVo add(HttpSession session,@RequestParam("productId") Integer productId, @RequestParam("count") Integer quantity){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        CartVo cartVo = cartService.create(userDto.getId(),productId,quantity);
        return ResultVo.createBySuccess(cartVo);
    }
    //全选

    @GetMapping("/select_all")
    public ResultVo selectAll(HttpSession session){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        CartVo cartVo = cartService.selectOrUnSelect(userDto.getId(),null,Const.CART_PRODUCT_CHECKED);
        return ResultVo.createBySuccess(cartVo);
    }
    //全反选
    @GetMapping("/un_select_all")
    public ResultVo unSelectAll(HttpSession session){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        CartVo cartVo = cartService.selectOrUnSelect(userDto.getId(),null,Const.CART_PRODUCT_UNCHECKED);
        return ResultVo.createBySuccess(cartVo);
    }
    //单选
    @GetMapping("/select_product")
    public ResultVo selectProduct(HttpSession session,@RequestParam("productId") Integer productId){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        CartVo cartVo = cartService.selectOrUnSelect(userDto.getId(),productId,Const.CART_PRODUCT_CHECKED);
        return ResultVo.createBySuccess(cartVo);
    }
    //单独反选
    @GetMapping("/un_select_product")
    public ResultVo unSelectProduct(HttpSession session,@RequestParam("productId") Integer productId){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        CartVo cartVo = cartService.selectOrUnSelect(userDto.getId(),productId,Const.CART_PRODUCT_UNCHECKED);
        return ResultVo.createBySuccess(cartVo);
    }

    //获取购物车产品的总数量
    @GetMapping("get_cart_product_count")
    public ResultVo getCartProductCount(HttpSession session){
        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        Integer count = cartService.getCartProductCount(userDto.getId());
        return ResultVo.createBySuccess(count);
    }



}
