package com.bjpowernode.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.reggie.entity.Employee;
import com.bjpowernode.reggie.mapper.EmployeeMapper;
import com.bjpowernode.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author qjl
 * @create 2022-09-21 21:11
 *
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{
}
