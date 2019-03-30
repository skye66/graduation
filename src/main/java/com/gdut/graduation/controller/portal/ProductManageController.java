package com.gdut.graduation.controller.portal;

import com.gdut.graduation.dto.ProductDto;
import com.gdut.graduation.enums.ResultEnum;
import com.gdut.graduation.exception.GraduationException;
import com.gdut.graduation.pojo.Product;
import com.gdut.graduation.serveice.ProductService;
import com.gdut.graduation.vo.ResultVo;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Description 后台产品管理控制器
 * @Author Skye
 * @Date 2019/3/30 9:30
 * @Version 1.0
 **/
@RestController
@RequestMapping("/manage/product")
@Slf4j
public class ProductManageController {
    @Autowired
    private ProductService productService;


    @PostMapping("/save")
    public ResultVo ProductSave(Product product){
        if (product == null) throw new GraduationException(ResultEnum.PRODUCT_PARAM_ERROR);
        ProductDto productDto = productService.saveOrUpdateProduct(product);
        return ResultVo.createBySuccess(productDto);
    }


    @GetMapping("/set_sale_status")
    public ResultVo setSaleStatus(@RequestParam("product_id") Integer productId, Integer status){
        if (productId==null||status==null){
            log.error("【产品后台管理】产品id错误或产品状态为空");
            throw new GraduationException(ResultEnum.PRODUCT_PARAM_ERROR);
        }
        ProductDto productDto = productService.setSaleStatus(productId,status);
        return ResultVo.createBySuccess(productDto);
    }


    @GetMapping("detail")
    public ResultVo detail(Integer productId){
        if (productId == null){
            log.error("【产品后台管理】产品id错误");
            throw new GraduationException(ResultEnum.PRODUCT_PARAM_ERROR);
        }
        ProductDto productDto = productService.selectProduct(productId);
        return ResultVo.createBySuccess(productDto);

    }

    @GetMapping("/list")
    public ResultVo list(@RequestParam(value = "page_num",defaultValue = "1") Integer pageNum,@RequestParam(value = "page_size",defaultValue = "10") Integer pageSize){
        PageInfo pageInfo = productService.selectProductAll(pageNum,pageSize);
        return ResultVo.createBySuccess(pageInfo);

    }
    @GetMapping("/search")
    public ResultVo productSearch(@RequestParam(value = "product_name",required = false) String productName,
                                  @RequestParam(value = "product_id",required = false) Integer productId,
                                  @RequestParam(value = "page_num",defaultValue = "1") int pageNum,
                                  @RequestParam(value = "page_size",defaultValue = "10") int pageSize){
        PageInfo pageInfo = productService.searchProductList(productName,productId,pageNum,pageSize);
        return ResultVo.createBySuccess(pageInfo);
    }

    //todo upload



}
