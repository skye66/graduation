package com.gdut.graduation.controller.portal;

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
import java.util.Set;

/**
 * @Description 类目的控制器
 * @Author Skye
 * @Date 2019/3/26 21:29
 * @Version 1.0
 **/
@RestController
@RequestMapping("/manage/category")
@Slf4j
public class CategoryManageController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/information")
    public ResultVo information(@RequestParam("categoryId") Integer categoryId){
        if (categoryId ==null){
            log.error("【类目】类目id为空");
            throw new GraduationException(ResultEnum.CATEGORY_ID_EMPTY);
        }
        Category category = categoryService.getCategory(categoryId);
        return ResultVo.createBySuccess(category);
    }
    @PostMapping("/update")
    public ResultVo update(@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId,@RequestParam("categoryName") String categoryName ){

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        Category res = categoryService.updateCategory(category);
        return ResultVo.createBySuccess(res);
    }
    @PostMapping("/create")
    public ResultVo create(@RequestParam(value = "parentId",defaultValue = "0") Integer parentId,@RequestParam("categoryName") String categoryName){
        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        Category res = categoryService.createCategory(category);
        return ResultVo.createBySuccess(res);
    }
    @GetMapping("/delete")
    public ResultVo delete(@RequestParam("categoryId") Integer categoryId){
        if (categoryId==null) {
            log.error("【类目】类目id为空");
            throw new GraduationException(ResultEnum.CATEGORY_EMPTY);
        }
        boolean res = categoryService.deleteCategory(categoryId);
        if (res) return ResultVo.createBySuccess("删除类目成功");
        else return ResultVo.createByError(ResultEnum.CATEGORY_ID_EMPTY);
    }
    @GetMapping("/parallel")
    public ResultVo parallel(@RequestParam("categoryId") Integer categoryId){
        if (categoryId == null){
            log.error("【类目】id为空");
            throw new GraduationException(ResultEnum.CATEGORY_ID_EMPTY);
        }
        List<Category> categoryList = categoryService.getParallelId(categoryId);
        return ResultVo.createBySuccess(categoryList);
    }
    @GetMapping("/deep_children_category")
    public ResultVo deepChildrenCategory(@RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        Set<Category> categorySet = categoryService.getChildParallelCategory(categoryId);
        return ResultVo.createBySuccess(categorySet);
    }
    @GetMapping("/get_category")
    public ResultVo getCategory(@RequestParam("categoryId") Integer parentId){

        List<Category> categoryList = categoryService.getParallelId(parentId);
        return ResultVo.createBySuccess(categoryList);
    }
}
