package com.bjpowernode.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bjpowernode.reggie.common.BaseContext;
import com.bjpowernode.reggie.common.R;
import com.bjpowernode.reggie.entity.ShoppingCart;
import com.bjpowernode.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author qjl
 * @create 2022-09-25 14:25
 */
@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
//        查询用户id,将当前的用户id放到表中
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

//        查看当前添加的菜品或者套餐是否存在
//        查询条件，对于userid的查询
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> QueryWrapper = new LambdaQueryWrapper<>();
        QueryWrapper.eq(ShoppingCart::getUserId,currentId);


        if(dishId != null){
            QueryWrapper.eq(ShoppingCart::getDishId,dishId);
//            说明现在添加的是菜品

        }else {
//            说明现在添加的是套餐
            QueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(QueryWrapper);
        if(one != null){
//            说明当前添加的菜品存在
//            Integer number = shoppingCart.getNumber();
//            number = number +1;
//            shoppingCart.setNumber(number);
//        存在就number加1
            Integer number = one.getNumber();
            one.setNumber(number+1);
            shoppingCartService.updateById(one);
        }else {
//        不存在就放到表中
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }

        return R.success(one);
    }

    @PostMapping("/sub")
    public R<ShoppingCart> sub(@RequestBody ShoppingCart shoppingCart){
//        查询用户id,将当前的用户id放到表中
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

//        查看当前添加的菜品或者套餐是否存在
//        查询条件，对于userid的查询
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> QueryWrapper = new LambdaQueryWrapper<>();
        QueryWrapper.eq(ShoppingCart::getUserId,currentId);


        if(dishId != null){
            QueryWrapper.eq(ShoppingCart::getDishId,dishId);
//            说明现在添加的是菜品

        }else {
//            说明现在添加的是套餐
            QueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(QueryWrapper);
        if(one != null){
//            按下减少按钮对应的数量会进行减一
//            Integer number = shoppingCart.getNumber();
//            number = number +1;
//            shoppingCart.setNumber(number);
//        存在就number加1
            Integer number = one.getNumber();
            one.setNumber(number-1);
            shoppingCartService.updateById(one);
        }else {
//        不存在就放到表中
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }

        return R.success(one);
    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clear(){
        LambdaQueryWrapper<ShoppingCart> QueryWrapper = new LambdaQueryWrapper<>();
        QueryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(QueryWrapper);
        return R.success("清空购物车成功");



    }



}
