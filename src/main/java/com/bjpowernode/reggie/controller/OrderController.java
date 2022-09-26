package com.bjpowernode.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.reggie.common.R;
import com.bjpowernode.reggie.entity.Category;
import com.bjpowernode.reggie.entity.Orders;
import com.bjpowernode.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author qjl
 * @create 2022-09-25 15:39
 */
@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        orderService.submit(orders);
        return R.success("下单成功");

    }

    @GetMapping("/userPage")
    public R<Page> userpage(int page,int pageSize){
        //        分页构造器
        Page<Orders> pageinfo = new Page<>(page,pageSize);
//        排序构造
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Orders :: getCheckoutTime);
        orderService.page(pageinfo,queryWrapper);
        return R.success(pageinfo);


    }
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize){
        //        分页构造器
        Page<Orders> pageinfo = new Page<>(page,pageSize);
//        排序构造
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Orders :: getCheckoutTime);
        orderService.page(pageinfo,queryWrapper);
        return R.success(pageinfo);


    }

}
