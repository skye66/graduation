package com.gdut.graduation.controller;

import com.gdut.graduation.dto.ProductDto;
import com.gdut.graduation.enums.ResultEnum;
import com.gdut.graduation.exception.GraduationException;
import com.gdut.graduation.pojo.Product;
import com.gdut.graduation.serveice.ProductService;
import com.gdut.graduation.vo.ResultVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description product控制器
 * @Author Skye
 * @Date 2019/3/30 9:11
 * @Version 1.0
 **/
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/detail")
    public ResultVo detail(@RequestParam("productId") Integer productId){
        if (productId == null){
            log.error("【用户控制器】产品id为空");
            throw new GraduationException(ResultEnum.PRODUCT_PARAM_ERROR);
        }
        ProductDto productDto = productService.selectProduct(productId);
        return ResultVo.createBySuccess(productDto);
    }
    @GetMapping("/list")
    public ResultVo list(@RequestParam(value = "keyword",required = false) String keyword,
                         @RequestParam(value = "categoryId",required = false) Integer categoryId,
                         @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        PageInfo<List<ProductDto>> pageInfo = productService.searchProductListByCategoryKeyWord(categoryId, keyword, pageNum, pageSize);
        return ResultVo.createBySuccess(pageInfo);
    }


}
