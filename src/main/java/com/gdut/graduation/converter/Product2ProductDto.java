package com.gdut.graduation.converter;

import com.gdut.graduation.dto.ProductDto;
import com.gdut.graduation.pojo.Product;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 转换器
 * @Author Skye
 * @Date 2019/3/28 11:01
 * @Version 1.0
 **/
public class Product2ProductDto {
    private Product2ProductDto(){}
    public static ProductDto converter(Product product){
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product,productDto);
        return productDto;
    }
    public static List<ProductDto> converter(List<Product> productList){
        List<ProductDto> productDtoList = new ArrayList<>();

        for (Product product : productList) {
            productDtoList.add(converter(product));
        }
        return productDtoList;
    }
}
