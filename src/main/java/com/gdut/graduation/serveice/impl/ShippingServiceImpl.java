package com.gdut.graduation.serveice.impl;

import com.gdut.graduation.dao.ShippingMapper;
import com.gdut.graduation.enums.ResultEnum;
import com.gdut.graduation.exception.GraduationException;
import com.gdut.graduation.pojo.Shipping;
import com.gdut.graduation.serveice.ShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 收获地址服务
 * @Author Skye
 * @Date 2019/3/31 20:26
 * @Version 1.0
 **/
@Service
@Slf4j
public class ShippingServiceImpl implements ShippingService {
    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public Shipping add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int count = shippingMapper.insert(shipping);
        if (count == 0){
            log.error("【收货地址】新建地址失败");
            throw new GraduationException(ResultEnum.SHIPPING_INSERT_ERROR);
        }
        return shippingMapper.selectByShippingIdUserId(shipping.getId(),userId);
    }

    @Override
    public boolean del(Integer userId, Integer shippingId) {
        int count = shippingMapper.deleteByShippingIdUserId(shippingId, userId);
        if (count == 0){
            throw new GraduationException(ResultEnum.SHIPPING_DEL_ERROR);
        }
        return true;
    }

    @Override
    public Shipping update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int count = shippingMapper.updateByShippingIdUserId(shipping);
        if (count == 0){
            throw new GraduationException(ResultEnum.SHIPPING_UPDATE_ERROR);
        }
        return shippingMapper.selectByShippingIdUserId(shipping.getId(),userId);
    }

    @Override
    public Shipping select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(shippingId, userId);
        if (shipping==null){
            throw new GraduationException(ResultEnum.SHIPPING_SELECT_ERROR);
        }
        return shipping;
    }

    @Override
    public PageInfo<List<Shipping>> selectAll(Integer userId, int pageNum,int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        if (shippingList == null){
            throw new GraduationException(ResultEnum.SHIPPING_SELECT_ERROR);
        }
        PageInfo pageInfo = new PageInfo(shippingList);
        return pageInfo;
    }
}
