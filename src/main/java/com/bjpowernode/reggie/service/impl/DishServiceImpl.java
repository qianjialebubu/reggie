package com.bjpowernode.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.reggie.dto.DishDto;
import com.bjpowernode.reggie.entity.Dish;
import com.bjpowernode.reggie.entity.DishFlavor;
import com.bjpowernode.reggie.mapper.DishMapper;
import com.bjpowernode.reggie.service.DishFlavorService;
import com.bjpowernode.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qjl
 * @create 2022-09-23 16:06
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishService dishService;
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long dishId = dishDto.getId();

//        保存菜品口味表且保存其id，根据id将菜品的口味进行对应，更新，dish_flavor表可能一个菜品有多个对应的口味，所以需要进行批量化保存
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) ->{
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);


    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
//        查询基本信息，从dish表查询
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);


//        查询口味信息
        LambdaQueryWrapper<DishFlavor> QueryWrapper = new LambdaQueryWrapper<>();
        QueryWrapper.eq(DishFlavor::getDishId,dish.getId());
//        QueryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> flavors = dishFlavorService.list(QueryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
//        三步：更新基本信息，
//        清除原口味的的数据，对flavor的一个delete操作
//        设置当前的信息
        this.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) ->{
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);


    }
}
