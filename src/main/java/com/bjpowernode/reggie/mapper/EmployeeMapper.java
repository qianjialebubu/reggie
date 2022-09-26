package com.bjpowernode.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bjpowernode.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author qjl
 * @create 2022-09-21 21:09
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
