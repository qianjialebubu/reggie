package com.bjpowernode.reggie.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.reggie.common.CustomException;
import com.bjpowernode.reggie.entity.Category;
import com.bjpowernode.reggie.entity.Dish;
import com.bjpowernode.reggie.entity.Setmeal;
import com.bjpowernode.reggie.mapper.CategoryMapper;
import com.bjpowernode.reggie.service.CategoryService;
import com.bjpowernode.reggie.service.DishService;
import com.bjpowernode.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qjl
 * @create 2022-09-23 14:59
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        //进行查询操作,查询当前分类是否关联菜品，关联菜品就不能进行删除
        queryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(queryWrapper);
        if (count > 0) {
            throw  new CustomException("当前分类项关联了菜品，不能删除");



        }
        //进行查询操作,查询当前分类是否关联套餐，关联套餐就不能进行删除
        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId,id);
        int count1 = setmealService.count(queryWrapper1);
        if (count1 > 0){
            throw new CustomException("当前菜品关联了套餐，不可以进行删除");
        }
//        什么也没有关联，正常删除
        super.removeById(id);



    }
}
