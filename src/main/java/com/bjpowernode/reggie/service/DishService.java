package com.bjpowernode.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjpowernode.reggie.dto.DishDto;
import com.bjpowernode.reggie.entity.Dish;

import java.util.List;

/**
 * @author qjl
 * @create 2022-09-23 16:03
 */
public interface DishService extends IService<Dish> {
//    扩展方法，插入菜品以及口味数据
    public void saveWithFlavor(DishDto dishDto);
//    根据id查询菜品信息和口味信息。
    public DishDto getByIdWithFlavor(Long id);


    public void updateWithFlavor(DishDto dishDto);

    public void updateDishStatus(Integer status, List<Long> ids);

    public void deleteByIds(List<Long> ids);

    public void removeWithFlavor(List<Long> ids);
}
