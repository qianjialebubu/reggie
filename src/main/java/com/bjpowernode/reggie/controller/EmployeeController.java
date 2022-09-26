//加密结果：e10adc3949ba59abbe56e057f20f883e
package com.bjpowernode.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjpowernode.reggie.entity.Employee;
import com.bjpowernode.reggie.service.EmployeeService;
import com.bjpowernode.reggie.common.R;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author qjl
 * @create 2022-09-21 21:14
 * 员工登录
 */
@Slf4j

@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @PostMapping(value ="/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
//        MD5加密
        String password = employee.getPassword();
//        password = DigestUtils.md5DigestAsHex(password.getBytes());
//        查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
//        查询是否查到
        if(emp==null){
            return R.error("登录失败");
        }
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }
        if(emp.getStatus()==0){
            return R.error("账号禁用");
        }
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }

    @PostMapping(value ="/logout")
    public R<String> logout(HttpServletRequest request){
//        清理当前员工的id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
//    新增员工

//    @RequestBody主要用来接收前端传递给后端的json字符串中的数据的(请求体中的数据的)；
//    而最常用的使用请求体传参的无疑是POST请求了，
//    所以使用@RequestBody接收数据时，
//    一般都用POST方式进行提交。在后端的同一个接收方法里，
//    @RequestBody与@RequestParam()可以同时使用，@RequestBody最多只能有一个，
//    而@RequestParam()可以有多个。
//    链接：https://blog.csdn.net/justry_deng/article/details/80972817?ops_request_misc=%257B%2522request%255Fid%2522%253A%2522166383351416800192276557%2522%252C%2522scm%2522%253A%252220140713.130102334..%2522%257D&request_id=166383351416800192276557&biz_id=0&utm_medium=distribute.pc_search_result.none-task-blog-2~all~top_positive~default-1-80972817-null-null.142^v50^control,201^v3^control_1&utm_term=RequestBody&spm=1018.2226.3001.4187

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee){
        log.info("新增员工，员工信息为{}",employee.toString());
//        明文进行加密
        employee.setPassword("123456");
//        使用md5进行加密
//        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
////        获得id
//        Long id = (Long) request.getSession().getAttribute("employee");
//        employee.setCreateUser(id);
//        employee.setUpdateUser(id);

        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize, String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);
//        分页构造器
        Page pageinfo = new Page(page,pageSize);
//        条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
//        添加过滤条件

        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
//        排序,按照更新时间进行排序
        queryWrapper.orderByDesc(Employee::getUpdateTime);
//        执行查询操作
        employeeService.page(pageinfo,queryWrapper);

        return R.success(pageinfo);
    }

//    /禁用或者启用员工状态都是对数据库表的更新
//    修改员工的信息
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
//        Long empId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);
        long id = Thread.currentThread().getId();
        log.info("+++++++++++++++++++++修改员工信息的账户线程id是{}",id);
        log.info(employee.toString());
        return  R.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
//    @PathVariable说明是从路径进行的获取id
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据员工id查询员工，id={}",id);
        Employee employee = employeeService.getById(id);
        if(employee == null){
            return R.error("员工不存在");
        }
        return R.success(employee);
    }

}
