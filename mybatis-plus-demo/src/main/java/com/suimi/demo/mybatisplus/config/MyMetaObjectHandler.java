/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.mybatisplus.config;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author suimi
 * @date 2018-03-27
 */
@Slf4j
public class MyMetaObjectHandler extends MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("新增处理");
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("更新处理");
    }
}
