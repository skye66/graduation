package com.gdut.graduation.serveice;

import com.gdut.graduation.vo.CartVo;

import java.util.List;

/**
 * @Description 购物车服务接口
 * @Author Skye
 * @Date 2019/4/1 10:54
 * @Version 1.0
 **/
public interface CartService {
    /**
     * 创建购物车
     * @param userId 用户id
     * @param productId 产品id
     * @param count 购买数量
     * @return
     */
    CartVo create(Integer userId,Integer productId,Integer count);

    /**
     * 更新购物车
     * @param userId 用户id
     * @param productId 产品id
     * @param count 购买数量
     * @return
     */
    CartVo update(Integer userId,Integer productId,Integer count);

    /**
     * 删除购物车
     * @param userId 用户id
     * @param productIdList 产品id列表
     * @return
     */
    boolean delete(Integer userId, List productIdList);

    /**
     * 获取购物车列表
     * @param userId 用户id
     * @return
     */
    CartVo list(Integer userId);

    /**
     * 全选或者反选购物车中的产品
     * @param userId 用户id
     * @param productId 产品id
     * @param checked 是否选中1选中，0未选中
     * @return
     */
    CartVo selectOrUnSelect(Integer userId,Integer productId, Integer checked);

    /**
     * 获取购物车中的总数量
     * @param userId 用户id
     * @return
     */
    Integer getCartProductCount(Integer userId);
}
