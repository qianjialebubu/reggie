package com.bjpowernode.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjpowernode.reggie.entity.Orders;

/**
 * @author qjl
 * @create 2022-09-25 15:35
 */

public interface OrderService extends IService<Orders> {
    public void submit(Orders orders);
}
