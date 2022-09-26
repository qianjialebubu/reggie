package com.bjpowernode.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.reggie.common.R;
import com.bjpowernode.reggie.entity.Category;
import com.bjpowernode.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author qjl
 * @create 2022-09-23 15:01
 * 控制层需要调用service层的对象
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
//        输入是一个json数组，通过RequestBody进行转换才能得到
        categoryService.save(category);
        log.info("category={}", category);
        return R.success("新增分类成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize){
//        分页构造器
        Page<Category> pageinfo = new Page<>(page,pageSize);
//        排序构造
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category :: getSort);
        categoryService.page(pageinfo,queryWrapper);
        return R.success(pageinfo);

    }

    @DeleteMapping
    public R<String> delete(Long ids){
//        categoryService.removeById(ids);
        categoryService.remove(ids);
        log.info("-----------------------------------------------------------------------------------------------------------------");
        log.info("删除的菜品id={}",ids);
        return R.success("分类信息删除成功！");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息{}",category);
        categoryService.updateById(category);

        return R.success("修改分类信息成功");

    }

    /**
     * 根据条件查询分类
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
//        新建条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        添加条件
        lambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
//        添加排序条件,优先根据sort进行升序排序，进而根据更新时间进行降序排序
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return R.success(list);
    }

}
