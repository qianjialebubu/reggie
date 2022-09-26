package com.bjpowernode.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.reggie.entity.OrderDetail;
import com.bjpowernode.reggie.mapper.OrderDetailMapper;
import com.bjpowernode.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author qjl
 * @create 2022-09-25 15:38
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
