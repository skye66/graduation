package com.gdut.graduation.serveice.impl;

import com.gdut.graduation.dao.CartMapper;
import com.gdut.graduation.dao.ProductMapper;
import com.gdut.graduation.enums.ResultEnum;
import com.gdut.graduation.exception.GraduationException;
import com.gdut.graduation.pojo.Cart;
import com.gdut.graduation.pojo.Product;
import com.gdut.graduation.serveice.CartService;
import com.gdut.graduation.util.BigDecimalUtil;
import com.gdut.graduation.vo.CartProductVo;
import com.gdut.graduation.vo.CartVo;
import common.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Skye
 * @Date 2019/4/1 16:09
 * @Version 1.0
 **/
@Slf4j
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public CartVo create(Integer userId, Integer productId, Integer count) {
        Cart cart = cartMapper.selectByUserIdProductId(userId,productId);

        if (cart==null){
            Cart record = new Cart();
            record.setUserId(userId);
            record.setProductId(productId);
            record.setQuantity(count);
            int res = cartMapper.insert(record);
            if (res==0) {
                log.error("【购物车】创建失败");
                throw new GraduationException(ResultEnum.CART_ADD_ERROR);
            }
        }else {
            //已有购物车，则将数量相加
            cart.setQuantity(cart.getQuantity()+count);
            cartMapper.updateByPrimaryKey(cart);
        }

        return list(userId);
    }

    @Override
    public CartVo update(Integer userId, Integer productId, Integer count) {

        Cart cart = cartMapper.selectByUserIdProductId(userId,productId);
        if (cart==null){
            log.error("【购物车】更新失败");
            throw new GraduationException(ResultEnum.CART_UPDATE_ERROR);
        }
        cart.setQuantity(count);
        int res = cartMapper.updateByPrimaryKey(cart);
        if (res == 0) {
            log.error("【购物车】更新失败");
            throw new GraduationException(ResultEnum.CART_UPDATE_ERROR);
        }
        return list(userId);
    }

    @Override
    public boolean delete(Integer userId, List productIdList) {
        if (userId ==null||productIdList==null){
            throw new GraduationException(ResultEnum.CART_PARAM_ERROR);
        }
        int count = cartMapper.deleteByUserIdProductIds(userId,productIdList);
        if (count ==0)
            throw new GraduationException(ResultEnum.CART_DELETE_ERROR);
        return true;
    }

    @Override
    public CartVo list(Integer userId) {
        //1.先从数据库中获取购物车
        //2.查找数据库对应的产品详情
        //3.封装CartProductVo对象（单项总额等）
        //4.计算购物车总额、是否全选等
        if (userId==null){
            throw new GraduationException(ResultEnum.CART_PARAM_ERROR);
        }
        //1
        List <Cart> cartList = cartMapper.selectByUserId(userId);
        if (cartList==null) {
            log.error("【购物车】当前用户没有购物车信息");
            throw new GraduationException(ResultEnum.CART_NOT_EXISTS);
        }
        //2
        List<Integer> productIdList = new ArrayList<>();
        for (Cart cart:cartList){
            productIdList.add(cart.getProductId());
        }
        List<Product> productList = productMapper.selectByProductIds(productIdList);
        //使用hashmap直接根据产品id取出产品，减少复杂度
        Map<Integer, Product> productHashMap = new HashMap<>();
        for (Product  product: productList){
            productHashMap.put(product.getId(),product);
        }
        //3
        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = new ArrayList<>();
        BigDecimal cartTotalPrice=BigDecimal.ZERO;
        for (Cart cart : cartList){
            CartProductVo cartProductVo = new CartProductVo();
            cartProductVo.setId(cart.getId());
            cartProductVo.setUserId(cart.getUserId());
            cartProductVo.setProductId(cart.getProductId());

            cartProductVo.setProductChecked(cart.getChecked());
            //取出相对应的产品
            Product product = productHashMap.get(cart.getProductId());
            cartProductVo.setProductIcon(product.getIcon());
            cartProductVo.setProductName(product.getName());
            cartProductVo.setProductPrice(product.getPrice());
            cartProductVo.setProductStatus(product.getStatus());
            cartProductVo.setProductStock(product.getStock());
            int buyLimit = 0;
            if (cart.getQuantity()<product.getStock()){
                buyLimit=cart.getQuantity();
                cartProductVo.setLimitQuantity(Const.LIMIT_CART_COUNT_SUCCESS);
            }else {
                buyLimit=product.getStock();
                cartProductVo.setLimitQuantity(Const.LIMIT_CART_COUNT_FAIL);
                //更新购物车为有效库存
                cartMapper.updateQuantityByCartId(cart.getId(), product.getStock());
            }
            //设置有效购买数量
            cartProductVo.setQuantity(buyLimit);
            //计算总额
            BigDecimal totalPrice = BigDecimalUtil.mul(cart.getQuantity(),product.getPrice().doubleValue());
            cartProductVo.setProductTotalPrice(totalPrice);
            //当前产品被勾选则将其添加到购物车总额中
            if (cart.getChecked()==Const.CART_PRODUCT_CHECKED) {
                cartTotalPrice = BigDecimalUtil.add(totalPrice.doubleValue(),cartTotalPrice.doubleValue());
            }
            //将分装后的数据添加到购物车vo对象中
            cartProductVoList.add(cartProductVo);
        }
        cartVo.setCartTotalPrice(cartTotalPrice);
        List<Cart> uncheckedList = cartMapper.selectUnCheckedCartByUserId(userId);
        if (uncheckedList==null||uncheckedList.size()==0){
            cartVo.setAllChecked(true);
        }else cartVo.setAllChecked(false);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setImageHost(Const.image_host);
        return cartVo;
    }

    @Override
    public CartVo selectOrUnSelect(Integer userId, Integer productId, Integer checked) {
        if (userId==null||checked==null){
            log.error("【购物车】参数错误");
            throw new GraduationException(ResultEnum.CART_PARAM_ERROR);
        }
        int count = cartMapper.CheckedOrUnCheckedUpdate(userId,productId,checked);
        if (count == 0){
            log.error("【购物车】更新失败");
            throw new GraduationException(ResultEnum.CART_UPDATE_ERROR);
        }
        return list(userId);
    }

    @Override
    public Integer getCartProductCount(Integer userId) {
        if (userId==null) {
            log.error("【购物车】参数错误");
            throw new GraduationException(ResultEnum.CART_PARAM_ERROR);
        }
        return cartMapper.selectCartProductCount(userId);
    }
}
