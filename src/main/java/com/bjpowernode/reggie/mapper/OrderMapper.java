package com.bjpowernode.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjpowernode.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author qjl
 * @create 2022-09-25 15:32
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
