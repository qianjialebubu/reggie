package com.bjpowernode.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.reggie.common.BaseContext;
import com.bjpowernode.reggie.common.CustomException;
import com.bjpowernode.reggie.entity.*;
import com.bjpowernode.reggie.mapper.OrderMapper;
import com.bjpowernode.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @Override
    @Transactional
    public void submit(Orders orders) {
        // 获取用户的id。
        Long userId = BaseContext.getCurrentId();
        // 查询购物车菜品或套餐数据。
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);
        // 查询用户数据
        LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(User::getId, userId);
        User user = userService.getOne(queryWrapper1);
        // 查询用户地址数据。
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("用户地址有误，不能下单");
        }

        // 订单号
        long orderId = IdWorker.getId();
        // 计算所有菜品或套餐的总金额。
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> orderDetails =
                shoppingCartList.stream()
                        .map(
                                (shoppingCart) -> {
                                    OrderDetail orderDetail = new OrderDetail();

                                    orderDetail.setOrderId(orderId);
                                    orderDetail.setNumber(shoppingCart.getNumber());
                                    orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
                                    orderDetail.setDishId(shoppingCart.getDishId());
                                    orderDetail.setSetmealId(shoppingCart.getSetmealId());
                                    orderDetail.setName(shoppingCart.getName());
                                    orderDetail.setImage(shoppingCart.getImage());
                                    orderDetail.setAmount(shoppingCart.getAmount());
                                    // 计算总金额
                                    amount.addAndGet(
                                            shoppingCart
                                                    .getAmount()
                                                    .multiply(new BigDecimal(shoppingCart.getNumber()))
                                                    .intValue());
                                    return orderDetail;
                                })
                        .collect(Collectors.toList());

        orders.setId(orderId);
        orders.setNumber(String.valueOf(orderId));
        orders.setStatus(2);
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(
                (addressBook.getProvinceCode() == null ? "" : addressBook.getProvinceCode())
                        + (addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                        + (addressBook.getCityCode() == null ? "" : addressBook.getCityCode())
                        + (addressBook.getCityName() == null ? "" : addressBook.getCityName()));
        orders.setAmount(new BigDecimal(amount.get()));
        // 向订单表（orders表）中插入数据，一条数据。
        this.save(orders);
        // 向订单明细表插入数据，多条数据。
        orderDetailService.saveBatch(orderDetails);
        // 清除购物车数据。
        shoppingCartService.remove(queryWrapper);
    }


}