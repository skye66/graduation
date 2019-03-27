package com.gdut.graduation.dao;

import com.gdut.graduation.pojo.Category;
import org.omg.CORBA.INTERNAL;

import java.util.List;

/**
 * @Description
 * @Author Skye
 * @Date 2019/3/26 17:30
 * @Version 1.0
 **/
public interface CategoryMapper {
    /**
     * 通过主键选择类目
     * @param id
     * @return
     */
    Category selectById(Integer id);

    /**
     * 更新类目的记录
     * @param category
     * @return
     */
    Integer updateByCategory(Category category);

    /**
     * 通过主键id，删除类目
     * @param id
     * @return
     */
    Integer deleteByCategory(Integer id);

    /**
     * 插入一条类目记录
     * @param category
     * @return
     */
    int insert(Category category);

    /**
     * 通过父级id，查找所以子节点（平级目录的遍历）
     * @param parentId
     * @return
     */
    List<Category> selectParallelId(Integer parentId);
}
