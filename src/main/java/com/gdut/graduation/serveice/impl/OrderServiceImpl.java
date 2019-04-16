package com.gdut.graduation.serveice.impl;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.gdut.graduation.configration.AlipayProperties;
import com.gdut.graduation.configration.ProjectConfig;
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
import com.gdut.graduation.vo.ResultVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import common.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
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
    @Autowired
    private PayInfoMapper payInfoMapper;
    @Autowired
    private AlipayProperties alipayProperties;

    @Autowired
    private ProjectConfig projectConfig;

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
        orderVo.setPaymentTypeDesc(Const.PaymentStatusEnum.codeOf(order.getPaymentType()).getMsg());

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
        orderProductVo.setProductTotalPrice(payment);
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
        PageHelper.startPage(pageNum, pageSize,Const.ORDER_BY_CREATE_TIME_DESC);
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
        PageHelper.startPage(pageNum, pageSize,Const.ORDER_BY_CREATE_TIME_DESC);
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

    @Override
    public ResultVo pay(String orderNo, Integer userId, String path) {
        Map<String, String> resultMap = new HashMap<>();
        Order order = orderMapper.selectByUserIdOrderNo(userId, orderNo);
        if (order == null) {
            return ResultVo.createByError("用户没有该订单");
        }
        resultMap.put("orderNo", String.valueOf(order.getOrderNo()));

        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = order.getOrderNo().toString();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = new StringBuilder().append("BMall扫码支付，订单号：").append(order.getOrderNo()).toString();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = new StringBuilder().append("订单").append(order.getOrderNo()).append("购买书籍共").append(totalAmount).toString();

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();

        List<OrderItem> orderItemList = orderItemMapper.selectByUserIdOrderNo(userId, orderNo);
        for (OrderItem orderItem : orderItemList
        ) {
            GoodsDetail goods1 = GoodsDetail.newInstance(orderItem.getProductId().toString(), orderItem.getProductName(),
                    BigDecimalUtil.mul(orderItem.getTotalPrice().doubleValue(), new Double(100).doubleValue()).longValue(),
                    orderItem.getQuantity());
            goodsDetailList.add(goods1);

        }
//        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
//        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx小面包", 1000, 1);
//        // 创建好一个商品后添加至商品明细列表
//        goodsDetailList.add(goods1);
//
//        // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.00元，购买了两件
//        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
//        goodsDetailList.add(goods2);

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(alipayProperties.getUrl())// 支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);


        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                //判断pat路径是否存在，不存在则创建
                path = projectConfig.getPath();
                File folder = new File(path);
                if (!folder.exists()) {
                    folder.setWritable(true);
                    folder.mkdirs();
                }
                // 需要修改为运行机器上的路径
                String qrPath = String.format(path + "/qr-%s.png", response.getOutTradeNo());
                String qrFileName = String.format("qr-%s.png", response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);

                //拼接host和二维码的名字
                String qrUrl = projectConfig.getImageHost()+qrFileName;
                resultMap.put("qrUrl", qrUrl);
                return ResultVo.createBySuccess(resultMap);
            case FAILED:
                log.error("支付宝预下单失败!!!");
                return ResultVo.createByError("支付宝预下单失败!!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return ResultVo.createByError("系统异常，预下单状态未知!!!");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                return ResultVo.createByError("不支持的交易状态，交易返回异常!!!");
        }
    }
    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (!StringUtils.isEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }

    @Override
    public ResultVo aliCallBack(Map<String, String> params) {
        String orderNo = String.valueOf(params.get("out_trade_no"));
        String tradeNo = params.get("trade_no");
        String tradeStatus = params.get("trade_status");
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            return ResultVo.createByError("非BMall的订单，回调忽略");
        }
        if (order.getStatus() >= Const.OrderStatusEnum.PAID.getCode()){
            return ResultVo.createBySuccess("支付宝重复调用");
        }
        if (Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)){
            order.setPaymentTime(DateTimeUtil.strToDateTime(params.get("gmt_payment")));
            order.setStatus(Const.OrderStatusEnum.PAID.getCode());
            orderMapper.updateByPrimaryKey(order);
        }
        PayInfo payInfo = new PayInfo();
        payInfo.setUserId(order.getUserId());
        payInfo.setOrderNo(order.getOrderNo());
        payInfo.setPayPlatform(Const.PayPlatformEnum.ALIPAY.getCode());
        payInfo.setPlatformNumber(tradeNo);
        payInfo.setPlatformStatus(tradeStatus);

        payInfoMapper.insert(payInfo);
        return ResultVo.createBySuccess();
    }
}
