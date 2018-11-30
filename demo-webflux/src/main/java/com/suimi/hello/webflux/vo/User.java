/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.webflux.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author suimi
 * @date 2018/11/28
 */
@NoArgsConstructor
@AllArgsConstructor
@Data public class User {
    private String id;
    private String name;
    private int age;
}
