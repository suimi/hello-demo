/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.shelldemo.service;

import org.springframework.stereotype.Service;

/**
 * @author suimi
 * @date 2019/1/18
 */
@Service public class MathService {

    public int add(int a, int b) {
        return a + b;
    }

    public int reduce(int a, int b) {
        return a - b;
    }

    public int mutl(int a, int b) {
        return a * b;
    }
}
