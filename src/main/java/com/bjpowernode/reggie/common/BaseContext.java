package com.bjpowernode.reggie.common;

/**
 * @author qjl
 * @create 2022-09-23 11:56
 * 保存和获取当前登录用户的id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static Long id1;
    public static Long id2;
    public static void setCurrentId(Long  id) {
        id1 = id;
        System.out.println("输入的id是"+id);
        id = null;
//        threadLocal.set(id);
    }
    public static Long getCurrentId() {
//        return threadLocal.get();
        id2 = id1;
//        clear();
        System.out.println("输出的id是"+id2);
        return id2;

    }
    public static void clear(){

        id1 = null;
    }

}
