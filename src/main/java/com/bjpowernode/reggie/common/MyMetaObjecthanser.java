package com.bjpowernode.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author qjl
 * @create 2022-09-23 10:55
 */
@Component
@Slf4j
public class MyMetaObjecthanser implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充Insert");
        log.info("Insert前= "+metaObject.toString());
//        long id = Thread.currentThread().getId();
//        log.info("++++++++++++++++++公共填充字段的线程id={}",id);

        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        System.out.println("BaseContext.getCurrentId()============================"+BaseContext.getCurrentId());
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
        log.info("Insert后= "+metaObject.toString());


    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充Update");
        metaObject.setValue("updateTime", LocalDateTime.now());

        metaObject.setValue("updateUser",BaseContext.getCurrentId());
        log.info("Insert前= "+metaObject.toString());
        long id = Thread.currentThread().getId();
        log.info("++++++++++++++++++公共填充字段的线程id={}",id);

        log.info("Update"+metaObject.toString());

    }
}
