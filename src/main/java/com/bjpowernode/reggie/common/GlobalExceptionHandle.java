package com.bjpowernode.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @author qjl
 * @create 2022-09-22 16:25
 */
//表示捕获带有注解RestController的方法
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandle {
    /**
     * 异常处理,处理这个异常的方法SQLIntegrityConstraintViolationException
     * R.error()的信息会显示在前端页面上
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
//            按照空格拆分输出报错语句，取其下标为2的信息为字段名
            String[] split = ex.getMessage().split(" ");
            String s = split[2];
            String s1 = s + "已经存在";
            return R.error(s1);

        }
        return R.error("未知错误");

    }

    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.info(ex.getMessage());

        return R.error(ex.getMessage());

    }

}
