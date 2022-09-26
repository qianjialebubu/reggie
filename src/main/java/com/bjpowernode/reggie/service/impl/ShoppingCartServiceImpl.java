package com.bjpowernode.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.reggie.entity.ShoppingCart;
import com.bjpowernode.reggie.mapper.ShoppingCartMapper;
import com.bjpowernode.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author qjl
 * @create 2022-09-25 14:24
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
