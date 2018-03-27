/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.mybatisplus.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IEnum;

public enum PhoneEnum implements IEnum {
    CMCC("10086", "中国移动"), CUCC("10010", "中国联通"), CT("10000", "中国电信"),;

    private String value;

    private String desc;

    PhoneEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public Serializable getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }
}
