package com.gdut.graduation.serveice;

import com.gdut.graduation.pojo.Category;

import java.util.List;

/**
 * @Description 类目接口
 * @Author Skye
 * @Date 2019/3/26 20:33
 * @Version 1.0
 **/
public interface CategoryService {
    /**
     * 通过类目id获取类目的信息
     * @param categoryId
     * @return
     */
    Category getCategory(int categoryId);

    /**
     * 查找所有的同级节点
     * @param categoryId
     * @return
     */
    List<Category> getParallelId(int categoryId);

    /**
     * 更新类目的信息
     * @param category
     * @return
     */
    Category updateCategory(Category category);

    /**
     * 通过类目id删除类目记录
     * @param categoryId
     * @return
     */
    boolean deleteCategory(int categoryId);

    /**
     * 添加类目记录
     * @param category
     * @return
     */
    Category createCategory(Category category);
}
