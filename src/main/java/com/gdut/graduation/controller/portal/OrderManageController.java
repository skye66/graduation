package com.gdut.graduation.controller.portal;

import com.gdut.graduation.serveice.OrderService;
import com.gdut.graduation.vo.OrderVo;
import com.gdut.graduation.vo.ResultVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @Description
 * @Author Skye
 * @Date 2019/4/4 10:41
 * @Version 1.0
 **/
@RestController
@RequestMapping("/manage/order")
public class OrderManageController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("/list")
    public ResultVo orderList(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                              @RequestParam(value = "pageSize",defaultValue = "10") int pageSize){
//        UserDto userDto = (UserDto) session.getAttribute(Const.CURRENT_USER);
        PageInfo pageInfo = orderService.manageList(pageNum, pageSize);
        return ResultVo.createBySuccess(pageInfo);

    }
    @RequestMapping("/detail")
    public ResultVo orderDetail(@RequestParam(value = "orderNo")String orderNo){
        OrderVo orderVo = orderService.manageOrderDetail(orderNo);
        return ResultVo.createBySuccess(orderVo);
    }
    @RequestMapping("/search")
    public ResultVo orderSearch(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize",defaultValue = "10")int pageSize,
                                @RequestParam(value = "orderNo") String orderNo){
        PageInfo pageInfo = orderService.manageSearch(orderNo,pageNum,pageSize);
        return ResultVo.createBySuccess(pageInfo);
    }
    @RequestMapping("/send_goods")
    public ResultVo orderSendGoods(
                                   @RequestParam(value = "orderNo") String orderNo){
        String res = orderService.manageSendGoods(orderNo);
        return ResultVo.createBySuccess(res);
    }
}
