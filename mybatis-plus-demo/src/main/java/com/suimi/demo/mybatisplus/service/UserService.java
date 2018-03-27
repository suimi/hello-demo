/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.mybatisplus.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.suimi.demo.mybatisplus.entity.User;
import com.suimi.demo.mybatisplus.mapper.UserMapper;

/**
 * @author suimi
 * @date 2018-03-27
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    public User find(long id) {
        return baseMapper.selectById(id);
    }

    public List<User> findByName(String name) {
        return baseMapper.selectByName(name);
    }

    public Page<User> findByName(Page<User> page, String name) {
        return page.setRecords(baseMapper.selectByName(page, name));
    }

    public boolean deleteAll() {
        return retBool(baseMapper.deleteAll());
    }

}
