package com.gdut.graduation.dao;

import com.gdut.graduation.pojo.Category;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


/**
 * @Description 测试类目的dao
 * @Author Skye
 * @Date 2019/3/26 19:29
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
@Rollback
public class CategoryMapperTest {
    @Autowired
    private CategoryMapper categoryMapper;
//    Logger logger = LoggerFactory.getLogger(CategoryMapperTest.class);

    @Test
    public void selectById(){
        Category category = categoryMapper.selectById(20);
        Assert.assertNotNull(category);
    }
    @Test
    public void updateByCategory(){
        Category category = categoryMapper.selectById(20);
        category.setId(20);
        category.setName("猪肉");
        category.setParentId(2);
        int count = categoryMapper.updateByCategory(category);
        Assert.assertEquals(count, 1);
    }
    @Test
    public void deleteByCategory(){
        int count = categoryMapper.deleteByCategory(33);
        Assert.assertNotEquals(count,0);
    }
    @Test
    public void insert(){
        Category category = new Category();
        category.setId(1001);
        category.setParentId(2);
        category.setName("零食");
        int count = categoryMapper.insert(category);

        Assert.assertNotEquals(count,0);

    }
    @Test
    public void selectParallelId(){
        List<Category> categoryList = categoryMapper.selectParallelId(1);
        Assert.assertNotEquals(categoryList.size(),0);
    }


}