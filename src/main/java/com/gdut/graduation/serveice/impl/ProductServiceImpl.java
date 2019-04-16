package com.gdut.graduation.serveice.impl;

import com.gdut.graduation.converter.Product2ProductDto;
import com.gdut.graduation.dao.ProductMapper;
import com.gdut.graduation.dto.ProductDto;
import com.gdut.graduation.enums.ResultEnum;
import com.gdut.graduation.exception.GraduationException;
import com.gdut.graduation.pojo.Category;
import com.gdut.graduation.pojo.Product;
import com.gdut.graduation.serveice.CategoryService;
import com.gdut.graduation.serveice.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import common.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Description
 * @Author Skye
 * @Date 2019/3/28 10:51
 * @Version 1.0
 **/
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryService categoryService;
    @Override
    public ProductDto selectProduct(Integer id) {
        if (id==null) {
            log.error("【产品】产品id为空");
            throw new GraduationException(ResultEnum.PRODUCT_PARAM_ERROR);
        }
        Product product = productMapper.selectByPrimaryKey(id);
        if (product == null){
            log.error("【产品】不存在该产品");
            throw new GraduationException(ResultEnum.PRODUCT_NOT_EXISTS);
        }
        return Product2ProductDto.converter(product);
    }

    @Override
    public PageInfo<List<ProductDto>> selectProductAll(int pageNum, int pageSize) {

        //PageHelper
        //填充sql语句
        //PageInfo结尾
        PageHelper.startPage(pageNum, pageSize,Const.ORDER_BY_CREATE_TIME_DESC);
        List<Product> productList = productMapper.selectAll();
        List<ProductDto> productDtoList = Product2ProductDto.converter(productList);
        PageInfo pageInfo = new PageInfo<>(productList);
        pageInfo.setList(productDtoList);
        return pageInfo;
    }

    @Override
    public PageInfo<List<ProductDto>> selectProductUpAll(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList = productMapper.selectByStatus(Const.ON_SALE);
        List<ProductDto> productDtoList = Product2ProductDto.converter(productList);
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productDtoList);
        return pageInfo;
    }

    @Override
    public boolean deleteProduct(Integer id) {
        int count = productMapper.deleteByPrimaryKey(id);
        if (count==0){
            log.error("【产品】删除错误，不存在该产品");
            throw new GraduationException(ResultEnum.PRODUCT_NOT_EXISTS);
        }
        return true;
    }

    @Override
    public ProductDto saveOrUpdateProduct(Product product) {
        if (product==null) {
            log.error("【产品】参数错误");
            throw new GraduationException(ResultEnum.PRODUCT_PARAM_ERROR);
        }
        if (product.getId()!=null){
            int count = productMapper.updateByPrimaryKey(product);
            if (count > 0){
                return Product2ProductDto.converter(productMapper.selectByPrimaryKey(product.getId()));
            }else throw new GraduationException(ResultEnum.PRODUCT_UPDATE_CREATE_ERROR);
        }else {
            int id = productMapper.insert(product);
            if (id == 0) {
                log.error("【产品】更新或者增加错误");
                throw new GraduationException(ResultEnum.PRODUCT_UPDATE_CREATE_ERROR);
            }
            return Product2ProductDto.converter(productMapper.selectByPrimaryKey(id));
        }
    }

    @Override
    public ProductDto setSaleStatus(Integer productId, Integer status) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product==null){
            log.error("【产品】不存在该产品");
            throw new GraduationException(ResultEnum.PRODUCT_NOT_EXISTS);
        }
        product.setStatus(status);
        productMapper.updateByPrimaryKey(product);
        return Product2ProductDto.converter(product);
    }

    @Override
    public PageInfo<List<ProductDto>> searchProductList(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (productName!=null){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByProductNameId(productName,productId);
        List<ProductDto> productDtoList = Product2ProductDto.converter(productList);
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productDtoList);
        return pageInfo;
    }

    @Override
    public PageInfo<List<ProductDto>> searchProductListByCategoryKeyWord(Integer categoryId, String keyword, int pageNum, int pageSize) {

        if (StringUtils.isEmpty(keyword)&&categoryId==null) throw new GraduationException(ResultEnum.PRODUCT_PARAM_ERROR);
        List<Integer> categoryIdList = new ArrayList<>();
        if (categoryId !=null){
            Set<Category> categorySet = categoryService.getChildParallelCategory(categoryId);
            if (CollectionUtils.isEmpty(categorySet) && StringUtils.isEmpty(keyword)){
                PageHelper.startPage(pageNum, pageSize);
                List<ProductDto> productDtoList = new ArrayList<>();
                PageInfo pageInfo = new PageInfo(productDtoList);
                return pageInfo;
            }
            for (Category category:categorySet){
                categoryIdList.add(category.getId());
            }
        }
        if (!StringUtils.isEmpty(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }


        PageHelper.startPage(pageNum, pageSize);
        List<Product> productList =productMapper.selectByProductNameCategoryIds(StringUtils.isEmpty(keyword)?null:keyword, categoryIdList.isEmpty()?null:categoryIdList);
        List<ProductDto> productDtoList = Product2ProductDto.converter(productList);
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productDtoList);
        return pageInfo;
    }
}
