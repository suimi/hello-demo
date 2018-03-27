/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.mybatisplus.mapper;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.suimi.demo.mybatisplus.entity.User;

/**
 * @author suimi
 * @date 2018-03-27
 */
public interface UserMapper extends SupperMapper<User> {

    /**
     * 自定义注入方法
     */
    int deleteAll();

    List<User> selectByName(Pagination page, String name);

    List<User> selectByName(String name);
}
