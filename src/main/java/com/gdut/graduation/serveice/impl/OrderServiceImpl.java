package com.gdut.graduation.serveice.impl;

import com.gdut.graduation.converter.OrderItem2OrderItemVo;
import com.gdut.graduation.converter.Shipping2ShippingVo;
import com.gdut.graduation.dao.*;
import com.gdut.graduation.enums.ResultEnum;
import com.gdut.graduation.exception.GraduationException;
import com.gdut.graduation.pojo.*;
import com.gdut.graduation.serveice.OrderService;
import com.gdut.graduation.util.BigDecimalUtil;
import com.gdut.graduation.util.DateTimeUtil;
import com.gdut.graduation.util.KeyUtil;
import com.gdut.graduation.vo.OrderItemVo;
import com.gdut.graduation.vo.OrderProductVo;
import com.gdut.graduation.vo.OrderVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import common.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Description
 * @Author Skye
 * @Date 2019/4/3 9:19
 * @Version 1.0
 **/
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public boolean queryOrderPayStatus(Integer userId, String orderNo) {
        Order order = orderMapper.selectByUserIdOrderNo(userId,orderNo);

        if(order==null) {
            log.error("【订单】不存在该订单");
            throw new GraduationException(ResultEnum.ORDER_NOT_EXISTS);
        }
        if (order.getStatus()>= Const.OrderStatusEnum.PAID.getCode()){
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public OrderVo createOrder(Integer userId, Integer shippingId) {
        //1.从购物车中获取购买的商品
        List<Cart> cartList = cartMapper.selectCheckedCartByUserId(userId);
        //2.将每一项转换成orderItem对象
        List<OrderItem> orderItemList = getOrderItemList(userId,cartList);
        //3.计算总金额
        BigDecimal payment=getOrderTotalPrice(orderItemList);
        //4.生成订单
        String orderNo = KeyUtil.getUniqueOrderNo();
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
        order.setShippingId(shippingId);
        order.setPostage(BigDecimal.ZERO);
        order.setPaymentType(Const.PaymentStatusEnum.ONLINE_PAY.getCode());
        order.setPayment(payment);
        int count = orderMapper.insert(order);
        if (count ==0) {
            log.error("【订单】创建失败");
            throw new GraduationException(ResultEnum.ORDER_CREATE_ERROR);
        }

        //5.批量插入数据库
        for (OrderItem orderItem : orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
        }
        int orderItemCount = orderItemMapper.batchInsert(orderItemList);
        if (orderItemCount == 0){
            log.error("【订单详情】创建失败");
            throw new GraduationException(ResultEnum.ORDER_CREATE_ERROR);
        }
        //6.扣库存
        for (OrderItem orderItem : orderItemList){
            Product product= productMapper.selectByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock()-orderItem.getQuantity());
            productMapper.updateByPrimaryKey(product);
        }
        //7.清空购物车
        for (Cart cart : cartList){
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
        //8.返回数据给前端
        OrderVo orderVo = assembleOrderVo(order,orderItemList);
        return orderVo;
    }
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList){
        BigDecimal payment=BigDecimal.ZERO;
        for (OrderItem orderItem : orderItemList){
            payment=BigDecimalUtil.add(payment.doubleValue(),orderItem.getTotalPrice().doubleValue());
        }
        return payment;
    }

    /**
     * 聚合购物车对象和购物车详情对象返回购物车视图对象
     * @param order
     * @param orderItemList
     * @return
     */
    private OrderVo assembleOrderVo(Order order,List<OrderItem> orderItemList){
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order,orderVo);
        List<OrderItemVo> orderItemVoList = new ArrayList<>();
        for (OrderItem orderItem : orderItemList){
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(orderItem,orderItemVo);
            orderItemVoList.add(orderItemVo);
        }

        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        if (shipping!=null) {
            orderVo.setReceiverName(shipping.getReceiverName());
            orderVo.setShippingVo(Shipping2ShippingVo.convert(shipping));
        }
        orderVo.setOrderItemVoList(orderItemVoList);
        orderVo.setCloseTime(DateTimeUtil.datetimeToStr(order.getCloseTime()));
        orderVo.setEndTime(DateTimeUtil.datetimeToStr(order.getEndTime()));
        orderVo.setPaymentTime(DateTimeUtil.datetimeToStr(order.getPaymentTime()));
        orderVo.setSendTime(DateTimeUtil.datetimeToStr(order.getSendTime()));
        orderVo.setCreateTime(DateTimeUtil.datetimeToStr(order.getCreateTime()));
        orderVo.setStatusDesc(Const.OrderStatusEnum.codeOf(order.getStatus()).getMsg());

        return orderVo;
    }

    /**
     * 封装购物车详情对象
     * @param userId
     * @param cartList 购物车列表信息，已被选中的即将被购买，下单的物品
     * @return
     */
    private List<OrderItem> getOrderItemList(Integer userId,List<Cart> cartList){
        List<OrderItem> orderItemList = new ArrayList<>();
        List<Integer> productIdList=new ArrayList<>();
        Map<Integer, Product> productMap = new HashMap<>();
        for (Cart  cart: cartList){
            productIdList.add(cart.getProductId());
        }
        List<Product> productList = productMapper.selectByProductIds(productIdList);
        for (Product product: productList){
            //需要校验产品的库存和状态
            if (product.getStatus()!=Const.ON_SALE){
                log.error("【订单】产品不是在售状态,id:{},name:{}",product.getId(),product.getName());
                throw new GraduationException(ResultEnum.PRODUCT_NOT_ON_SALE);
            }
            productMap.put(product.getId(),product);
        }

        for (Cart cart:cartList){
            OrderItem orderItem = new OrderItem();
            Product product = productMap.get(cart.getProductId());
            if (product == null) continue;
            orderItem.setUserId(userId);
            if (cart.getQuantity()>product.getStock()){
                log.error("【订单】产品库存不足");
                throw new GraduationException(ResultEnum.PRODUCT_STOCK_NOT_ENOUGH);
            }
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getIcon());
            orderItem.setProductId(product.getId());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(),cart.getQuantity()));
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    @Override
    public boolean cancel(Integer userId, String orderNo) {
        //判断订单的状态，只能是未支付的状态才可以取消订单
        Order order = orderMapper.selectByUserIdOrderNo(userId,orderNo);
        if (order==null){
            log.error("【订单】不存在该订单");
            throw new GraduationException(ResultEnum.ORDER_NOT_EXISTS);
        }
        if (order.getStatus() != Const.OrderStatusEnum.NO_PAY.getCode()){
            log.error("【订单】已支付或已取消");
            throw new GraduationException(ResultEnum.ORDER_STATUS_UPDATE_ERROR);
        }
        order.setStatus(Const.OrderStatusEnum.CANCELED.getCode());
        int count = orderMapper.updateByPrimaryKey(order);
        if (count != 0) {
            return true;
        }
        return false;
    }

    /**
     * 还没下单，用于获取购物车订单产品详情
     * @param userId
     * @return
     */
    @Override
    public OrderProductVo getOrderCartProduct(Integer userId) {
        List<Cart> cartList = cartMapper.selectByUserId(userId);
        if (cartList ==null) {
            throw new GraduationException(ResultEnum.CART_NOT_EXISTS);
        }
        OrderProductVo orderProductVo = new OrderProductVo();
        List<OrderItem> orderItemList = getOrderItemList(userId,cartList);
        List<OrderItemVo> orderItemVoList = OrderItem2OrderItemVo.converter(orderItemList);
        orderProductVo.setOrderItemVoList(orderItemVoList);
        BigDecimal payment = getOrderTotalPrice(orderItemList);
        orderProductVo.setTotalPrice(payment);
        orderProductVo.setImageHost(Const.image_host);
        return orderProductVo;
    }

    /**
     * 已下单，直接从数据库中获取订单详情
     * @param userId
     * @param orderNo
     * @return
     */
    @Override
    public OrderVo getOrderDetail(Integer userId, String orderNo) {
        if (userId==null||orderNo==null) throw new GraduationException(ResultEnum.PARAM_ERROR);
        Order order = orderMapper.selectByUserIdOrderNo(userId,orderNo);
        List<OrderItem> orderItemList = orderItemMapper.selectByUserIdOrderNo(userId,orderNo);
        OrderVo orderVo = assembleOrderVo(order, orderItemList);
        return orderVo;
    }

    @Override
    public PageInfo<List<OrderVo>> getOrderList(Integer userId, int pageNum, int pageSize) {
        if (userId==null) throw new GraduationException(ResultEnum.PARAM_ERROR);
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectByUserId(userId);
        List<OrderVo> orderVoList=assembleOrderVoList(userId,orderList);
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVoList);
        return pageInfo;
    }
    private   List<OrderVo>  assembleOrderVoList(Integer userId, List<Order> orderList){
        List<OrderVo> orderVoList = new ArrayList<>();
        for (Order order: orderList){
            List<OrderItem> orderItemList;
            if (userId!=null) {
                orderItemList = orderItemMapper.selectByUserIdOrderNo(userId, order.getOrderNo());
            }else {
                orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
            }
            OrderVo orderVo = assembleOrderVo(order,orderItemList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }

    @Override
    public PageInfo<List<OrderVo>> manageList(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectAll();
        //传入id为null时，查询所有的订单
        List<OrderVo> orderVoList = assembleOrderVoList(null,orderList);
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVoList);
        return pageInfo;
    }

    @Override
    public OrderVo manageOrderDetail(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order==null) {
            log.info("【订单】订单号不存在");
            throw new GraduationException(ResultEnum.ORDER_NOT_EXISTS);
        }
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
        OrderVo orderVo = assembleOrderVo(order,orderItemList);
        return orderVo;
    }

    @Override
    public PageInfo<List<OrderVo>> manageSearch(String orderNo, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order==null) {
            log.info("【订单】订单号不存在");
            throw new GraduationException(ResultEnum.ORDER_NOT_EXISTS);
        }
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(orderNo);
        OrderVo orderVo = assembleOrderVo(order,orderItemList);
        PageInfo pageInfo = new PageInfo(Arrays.asList(order));
        pageInfo.setList(Arrays.asList(orderVo));
        return pageInfo;
    }

    @Override
    public String manageSendGoods(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order==null) {
            log.info("【订单】订单号不存在");
            throw new GraduationException(ResultEnum.ORDER_NOT_EXISTS);
        }
        if (order.getStatus()==Const.OrderStatusEnum.PAID.getCode()){
            order.setStatus(Const.OrderStatusEnum.SHIPPED.getCode());
            orderMapper.updateByPrimaryKey(order);
            return "发货成功";
        }
        return "发货失败";
    }
}
