package com.bjpowernode.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.reggie.entity.User;
import com.bjpowernode.reggie.mapper.UserMapper;
import com.bjpowernode.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author qjl
 * @create 2022-09-24 21:54
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
