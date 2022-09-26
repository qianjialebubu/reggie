package com.bjpowernode.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.reggie.common.R;
import com.bjpowernode.reggie.dto.DishDto;
import com.bjpowernode.reggie.entity.Category;
import com.bjpowernode.reggie.entity.Dish;
import com.bjpowernode.reggie.entity.DishFlavor;
import com.bjpowernode.reggie.service.CategoryService;
import com.bjpowernode.reggie.service.DishFlavorService;
import com.bjpowernode.reggie.service.DishService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qjl
 * @create 2022-09-24 9:30
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;


    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    /**
     * DishDto是json数据
     */
    public R<String> save(@RequestBody DishDto dishDto){
        log.info("dishDto={}",dishDto);

        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 将菜品分类显示出来
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
//        分页插件
        Page<Dish> pageinfo = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name !=null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageinfo,queryWrapper);
        //把pageinfo的信息复制到dishDtoPage里面，忽略records
        //把dish类型的改变为DishDto类型的，以下主要就是为DishDto进行赋值操作。
//        需要自己处理records
        BeanUtils.copyProperties(pageinfo,dishDtoPage,"records");
        List<Dish> records = pageinfo.getRecords();
        List<DishDto> list= records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category!=null){

                String name1 = category.getName();
                dishDto.setCategoryName(name1);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     *
     * @param id
     * @return
     * 使用PathVariable 后面的id属性值注入到GetMapping的id中
     */
    @GetMapping("{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }


    /**
     * 修改菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info("dishDto={}",dishDto);

        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品成功");
    }

//    /**
//     * 根据条件查询菜品的数据,新建套餐时套餐菜品时的添加菜品会弹出具体菜品的种类。
//     * @param dish
//     * @return
//     */
//    @GetMapping("/list")
//    public R<List<Dish>> list1(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
////        只查询状态=1的菜品即在售的菜品
//        queryWrapper.eq(Dish::getStatus,1);
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//    }


    /**
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null ,Dish::getCategoryId,dish.getCategoryId());
        //添加条件，查询状态为1（起售状态）的菜品
        queryWrapper.eq(Dish::getStatus,1);

        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();//分类id
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            //当前菜品的id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);
            //SQL:select * from dish_flavor where dish_id = ?
            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }

//    @PostMapping("/status/0")
//    public R<DishDto> status(Long ids){
//        log.info("修改菜品的状态信息id为{}",ids);
//        DishDto dishDto = new DishDto();
//        List<Dish> list = dishService.list();
//        for(Dish dish : list){
//            BeanUtils.copyProperties(dish,dishDto);
//            dishDto.setStatus(0);
//        }
//        dishService.updateById(dishDto);
//        return R.success(dishDto);
//    }
//
//    @PostMapping("/status/1")
//    public R<DishDto> status1(Long ids){
//        log.info("修改菜品的状态信息id为{}",ids);
//        DishDto dishDto = new DishDto();
//        List<Dish> list = dishService.list();
//        for(Dish dish : list){
//            BeanUtils.copyProperties(dish,dishDto);
//            dishDto.setStatus(1);
//        }
//        dishService.updateById(dishDto);
//        return R.success(dishDto);
//    }

    @PostMapping("/status/{id}")
    public R<DishDto> status11(@PathVariable int id,Long ids){
        log.info("修改菜品的状态信息id为{}",ids);
        DishDto dishDto = new DishDto();
        List<Dish> list = dishService.list();
        for(Dish dish : list){
            BeanUtils.copyProperties(dish,dishDto);
            dishDto.setStatus(id);
        }
        dishService.updateById(dishDto);
        return R.success(dishDto);
    }


    @DeleteMapping
    public R<DishDto> delete(Long ids){
        log.info("修改菜品的状态信息id为{}",ids);
        DishDto dishDto = new DishDto();
        List<Dish> list = dishService.list();
        for(Dish dish : list){
            BeanUtils.copyProperties(dish,dishDto);

        }
        dishService.removeById(ids);
        return R.success(dishDto);

    }

}
