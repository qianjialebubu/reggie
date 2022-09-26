package com.bjpowernode.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjpowernode.reggie.dto.SetmealDto;
import com.bjpowernode.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author qjl
 * @create 2022-09-23 16:05
 */
public interface SetmealService extends IService<Setmeal> {
    public void saveWithDish(SetmealDto setmealDto);
    public void removeWithDish(List<Long> ids);

    public SetmealDto  getByIdWithFlavor(Long id);

    public void updateWithFlavor(SetmealDto setmealDto);
}
