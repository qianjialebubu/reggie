package com.bjpowernode.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.reggie.common.R;
import com.bjpowernode.reggie.dto.DishDto;
import com.bjpowernode.reggie.dto.SetmealDto;
import com.bjpowernode.reggie.entity.Category;
import com.bjpowernode.reggie.entity.Dish;
import com.bjpowernode.reggie.entity.Setmeal;
import com.bjpowernode.reggie.service.CategoryService;
import com.bjpowernode.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qjl
 * @create 2022-09-24 16:15
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealService setmealService;
    /**
     * 由页面传回json数据
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("setmealDto={}",setmealDto);
        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> save(int page, int pageSize, String name) {
//        分页插件
        Page<Setmeal> pageinfo = new Page<>(page,pageSize);
        Page<SetmealDto> dtopage = new Page<>();


        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
//        模糊查询
        queryWrapper.like(name !=null,Setmeal::getName,name);
//        排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageinfo,queryWrapper);
        //避免页面展示时套餐分类不显示汉字
        BeanUtils.copyProperties(pageinfo,dtopage,"records");
        List<Setmeal> records = pageinfo.getRecords();
        List<SetmealDto> list = records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);

//            得到分类的id
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null){
                String name1 = category.getName();
                setmealDto.setCategoryName(name1);

            }
            return setmealDto;

        }).collect(Collectors.toList());
        dtopage.setRecords(list);
        return R.success(dtopage);




    }

    /**
     * 查询套餐数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list( Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

    /**
     *
     * @param ids
     * @return
     * 删除时需要删除两张表
     */
    @DeleteMapping

    public R<String> delete(Long ids){

        boolean b = setmealService.removeById(ids);
        if(b){
            return R.success("套餐数据删除成功");
        }
        return R.error("套餐数据删除失");

    }


    /**
     * @author qjl
     * @param status
     * @param ids
     * @return
     * 对套餐的批量起售与批量停售
     */
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable Integer status, @RequestParam List<Long> ids){
        setmealService.updateSetmealStatus(status, ids);
        return R.success("修改成功");

    }


    /**
     *
     * @param id
     * @return
     * 对套餐信息的修改
     */
    @GetMapping("{id}")
    public R<SetmealDto> get(@PathVariable Long id){
        SetmealDto setmealDto = setmealService.getByIdWithFlavor(id);

        return R.success(setmealDto);
    }
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        setmealService.updateWithDish(setmealDto);
        return R.success("套餐信息修改成功");
    }


}
