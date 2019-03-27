package com.gdut.graduation.controller;

import com.gdut.graduation.enums.ResultEnum;
import com.gdut.graduation.exception.GraduationException;
import com.gdut.graduation.pojo.Category;
import com.gdut.graduation.serveice.CategoryService;
import com.gdut.graduation.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description 类目的控制器
 * @Author Skye
 * @Date 2019/3/26 21:29
 * @Version 1.0
 **/
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/information")
    public ResultVo information(@RequestParam("category_id") Integer categoryId){
        if (categoryId ==null){
            log.error("【类目】类目id为空");
            throw new GraduationException(ResultEnum.CATEGORY_ID_EMPTY);
        }
        Category category = categoryService.getCategory(categoryId);
        return ResultVo.createBySuccess(category);
    }
    @PostMapping("/update")
    public ResultVo update(Category category){
        if (StringUtils.isEmpty(category)||category.getId()==null||category.getParentId()==null){
            log.error("【类目】更新失败,{}",category);
            throw new GraduationException(ResultEnum.CATEGORY_UPDATE_ERROR);
        }
        Category res = categoryService.updateCategory(category);
        return ResultVo.createBySuccess(res);
    }
    @PostMapping("/create")
    public ResultVo create(Category category){
        if (StringUtils.isEmpty(category)||category.getId()==null||category.getParentId()==null){
            log.error("【类目】添加失败,{}",category);
            throw new GraduationException(ResultEnum.CATEGORY_CREATE_ERROR);
        }
        Category res = categoryService.createCategory(category);
        return ResultVo.createBySuccess(res);
    }
    @GetMapping("/delete")
    public ResultVo delete(@RequestParam("category_id") Integer categoryId){
        if (categoryId==null) {
            log.error("【类目】类目id为空");
            throw new GraduationException(ResultEnum.CATEGORY_EMPTY);
        }
        boolean res = categoryService.deleteCategory(categoryId);
        if (res) return ResultVo.createBySuccess("删除类目成功");
        else return ResultVo.createByError(ResultEnum.CATEGORY_ID_EMPTY);
    }
    @GetMapping("/parallel")
    public ResultVo parallel(@RequestParam("category_id") Integer categoryId){
        if (categoryId == null){
            log.error("【类目】id为空");
            throw new GraduationException(ResultEnum.CATEGORY_ID_EMPTY);
        }
        List<Category> categoryList = categoryService.getParallelId(categoryId);
        return ResultVo.createBySuccess(categoryList);
    }
}
