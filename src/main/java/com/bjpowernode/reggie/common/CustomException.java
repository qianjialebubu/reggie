package com.bjpowernode.reggie.common;

/**
 * @author qjl
 * @create 2022-09-23 19:23
 */

/**
 * 业务异常类
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }


}
