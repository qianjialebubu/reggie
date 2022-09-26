package com.bjpowernode.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjpowernode.reggie.entity.AddressBook;
import com.bjpowernode.reggie.mapper.AddressBookMapper;
import com.bjpowernode.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * @author qjl
 * @create 2022-09-25 12:44
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
