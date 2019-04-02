package com.gdut.graduation.serveice;

import com.gdut.graduation.dto.ProductDto;
import com.gdut.graduation.pojo.Product;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Description 产品服务（书）
 * @Author Skye
 * @Date 2019/3/28 9:21
 * @Version 1.0
 **/
public interface ProductService {
    /**
     * 通过产品id查询
     * @param id
     * @return
     */
    ProductDto selectProduct(Integer id);

    /**
     * 查找所有的产品并分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<List<ProductDto>> selectProductAll(int pageNum, int pageSize);

    /**
     * 查找所有的在售产品并分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<List<ProductDto>> selectProductUpAll(int pageNum, int pageSize);

    /**
     * 通过产品id删除某一个产品
     * @param id
     * @return
     */
    boolean deleteProduct(Integer id);

    /**
     * 保存或者更新产品的信息
     * @param product
     * @return
     */
    ProductDto saveOrUpdateProduct(Product product);


    /**
     * 上架或者下架产品
     */
    ProductDto setSaleStatus(Integer productId, Integer status);
    //加减库存
//    ProductDto increaseStock();

    /**
     *
     * @param productName 模糊查询名字，如果为null不参与查询
     * @param productId 精确匹配id，如果为null不参与查询（不能通知为空）
     * @param pageNum 分页的起始页码
     * @param pageSize 分页大小
     * @return
     */

    PageInfo<List<ProductDto>> searchProductList(String productName,Integer productId,int pageNum,int pageSize);

    /**
     *
     * @param categoryId 产品的类目的id
     * @param keyword 模糊查询产品名
     * @param pageNum 分页起始页
     * @param pageSize 分页大小
     * @return
     */
    PageInfo<List<ProductDto>> searchProductListByCategoryKeyWord(Integer categoryId, String keyword, int pageNum,int pageSize);


    //List<Product> selectProductInProductIds();
}
