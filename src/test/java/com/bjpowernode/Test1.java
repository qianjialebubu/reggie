package com.bjpowernode;

import org.junit.jupiter.api.Test;

/**
 * @author qjl
 * @create 2022-09-23 20:55
 */
public class Test1 {
    @Test
    public void test(){
        String filename = "abcdj.jpg";
        String s = filename.substring(filename.lastIndexOf("."));
        System.out.println(s);

    }
}
