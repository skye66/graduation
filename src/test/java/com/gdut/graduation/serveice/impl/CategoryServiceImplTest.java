package com.gdut.graduation.serveice.impl;

import com.gdut.graduation.pojo.Category;
import com.gdut.graduation.serveice.CategoryService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @Description 测试类目
 * @Author Skye
 * @Date 2019/3/26 21:15
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@Rollback
public class CategoryServiceImplTest {

    @Autowired
    private CategoryService categoryService;
    @Test
    public void getCategory() {
        Category category = categoryService.getCategory(20);
        Assert.assertNotNull(category);
    }

    @Test
    public void getParallelId() {
        List<Category> categoryList = categoryService.getParallelId(0);
        Assert.assertNotEquals(categoryList.size(),0);
    }

    @Test
    public void updateCategory() {
        Category category = categoryService.getCategory(20);
        category.setName("猪肉");
        category.setParentId(2);
        Category res = categoryService.updateCategory(category);
        Assert.assertNotNull(res);
    }

    @Test
    public void deleteCategory() {
        boolean res = categoryService.deleteCategory(33);
        Assert.assertTrue(res);
    }

    @Test
    public void createCategory() {
        Category category = new Category();
        category.setId(1003);
        category.setParentId(2);
        category.setName("零食");
        Category res = categoryService.createCategory(category);

        Assert.assertNotNull(res);
    }

    @Test
    public void getParallelAndChildId(){
        Set<Category> categorySet = categoryService.getChildParallelCategory(20);
        Assert.assertNotEquals(0,categorySet.size());
    }
}