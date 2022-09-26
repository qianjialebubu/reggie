package com.bjpowernode.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjpowernode.reggie.entity.Category;
import org.springframework.stereotype.Service;

/**
 * @author qjl
 * @create 2022-09-23 14:57
 */

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
