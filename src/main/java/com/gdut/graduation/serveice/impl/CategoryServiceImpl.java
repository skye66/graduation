package com.gdut.graduation.serveice.impl;

import com.gdut.graduation.dao.CategoryMapper;
import com.gdut.graduation.enums.ResultEnum;
import com.gdut.graduation.exception.GraduationException;
import com.gdut.graduation.pojo.Category;
import com.gdut.graduation.serveice.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public List<Category> getParallelId(int parentId) {

        List<Category> categoryList = categoryMapper.selectParallelId(parentId);
        return categoryList;
    }

    /**
     * 查找所有的子节点
     * @param parentId
     * @return
     */
    @Override
    public Set<Category> getChildParallelCategory(int parentId) {
        Set<Category> categorySet = new HashSet<>();
        Category category = new Category();
        category.setId(parentId);
        getChildParallelCategory(category,categorySet);
        return categorySet;
    }

    private void getChildParallelCategory(Category category, Set<Category> categorySet) {
        if (category==null) return;
        categorySet.add(category);
        List<Category> childCategory = getChildCategory(category.getId());
        for (Category item : childCategory) {
            if (!categorySet.contains(item)) {
                getChildParallelCategory(item, categorySet);
            }
        }
    }
    private  List<Category> getChildCategory(Integer parentId){
        List<Category> categoryList=null;
        if (parentId != null) {
            categoryList = categoryMapper.selectParallelId(parentId);
        }
        return categoryList;
    }

    @Override
    public Category updateCategory(Category category) {

        int count = categoryMapper.updateByCategorySelective(category);
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
