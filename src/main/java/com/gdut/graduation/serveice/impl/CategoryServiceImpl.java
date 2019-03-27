package com.gdut.graduation.serveice.impl;

import com.gdut.graduation.dao.CategoryMapper;
import com.gdut.graduation.enums.ResultEnum;
import com.gdut.graduation.exception.GraduationException;
import com.gdut.graduation.pojo.Category;
import com.gdut.graduation.serveice.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author Skye
 * @Date 2019/3/26 20:49
 * @Version 1.0
 **/
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public Category getCategory(int categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        if (category == null) {
            log.error("【类目】查询id为空");
            throw new GraduationException(ResultEnum.CATEGORY_ID_EMPTY);
        }
        return category;
    }

    @Override
    public List<Category> getParallelId(int categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        if (category==null){
            log.error("【类目】查询id为空");
            throw new GraduationException(ResultEnum.CATEGORY_ID_EMPTY);
        }

        List<Category> categoryList = categoryMapper.selectParallelId(category.getParentId());
        return categoryList;
    }

    @Override
    public Category updateCategory(Category category) {

        int count = categoryMapper.updateByCategory(category);
        if (count==0){
            log.error("【类目】更新失败");
            throw new GraduationException(ResultEnum.CATEGORY_UPDATE_ERROR);
        }
        return categoryMapper.selectById(category.getId());
    }

    @Override
    public boolean deleteCategory(int categoryId) {
        Category category = categoryMapper.selectById(categoryId);
        if (category==null){
            log.error("【类目】不存在");
            throw new GraduationException(ResultEnum.CATEGORY_EMPTY);
        }
        int count = categoryMapper.deleteByCategory(categoryId);
        if (count != 0) return true;
        return false;
    }

    @Override
    public Category createCategory(Category category) {
        int count = categoryMapper.insert(category);
        if (count == 0) {
            log.error("【类目】添加失败,{}",category);
            throw new GraduationException(ResultEnum.CATEGORY_EMPTY);
        }
        return categoryMapper.selectById(category.getId());
    }
}
