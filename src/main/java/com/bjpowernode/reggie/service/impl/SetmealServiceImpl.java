package com.bjpowernode.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.reggie.common.CustomException;
import com.bjpowernode.reggie.dto.DishDto;
import com.bjpowernode.reggie.dto.SetmealDto;
import com.bjpowernode.reggie.entity.Dish;
import com.bjpowernode.reggie.entity.DishFlavor;
import com.bjpowernode.reggie.entity.Setmeal;
import com.bjpowernode.reggie.entity.SetmealDish;
import com.bjpowernode.reggie.mapper.SetmealMapper;
import com.bjpowernode.reggie.service.SetmealDishService;
import com.bjpowernode.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qjl
 * @create 2022-09-23 16:07
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;


    /**
     * 新增套餐要在套餐表与套餐与菜品的关联表上进行增加
     * 增加transactional保证事务的一致性，因为要操作两张表
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐的基本信息
        this.save(setmealDto);
//        保存关联关系
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);


    }

    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
//        System.out.println("_________________________________________________________删除————————————————————");
        //删除数据要考虑是否存在菜品是停售状态，不能删除就抛出一个异常
//        先查询是否状态为1的数据有没有


        LambdaQueryWrapper<Setmeal> QueryWrapper = new LambdaQueryWrapper<>();
        QueryWrapper.in(Setmeal::getId,ids);
        QueryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(QueryWrapper);
        if (count > 0) {
            throw new CustomException("当前的套餐正在售卖中，不能删除");
        }
//        System.out.println("---------------------------------------------------删除1——————————————————————");
//        删除套餐数据
        this.removeByIds(ids);
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);


//        关联数据
        setmealDishService.remove(lambdaQueryWrapper);

    }

    @Override
    @Transactional
    public SetmealDto getByIdWithFlavor(Long id) {

        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        return setmealDto;
    }

    @Override
    public void updateWithFlavor(SetmealDto setmealDto) {
////        this.updateById(setmealDto);
////        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
////        queryWrapper.eq(SetmealDish::getDishId,setmealDto.getId());
////        setmealDishService.remove(queryWrapper);
////        SetmealDish setmealDish = new SetmealDish();
////        BeanUtils.copyProperties(setmealDto,setmealDish);
////        setmealDishService.save(setmealDish);
////        三步：更新基本信息，
//////        清除原口味的的数据，对flavor的一个delete操作
//////        设置当前的信息
//        this.updateById(setmealDto);
//
////        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
////        queryWrapper.eq(DishFlavor::getDishId, setmealDto.getId());
////        setmealDishService.remove(queryWrapper);
//
//        List<DishFlavor> flavors = dishDto.getFlavors();
//        flavors = flavors.stream().map((item) -> {
//            item.setDishId(dishDto.getId());
//            return item;
//        }).collect(Collectors.toList());
//        dishFlavorService.saveBatch(flavors);
//    }



//        }



        }


}
