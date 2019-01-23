/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.shelldemo.controller;

import com.suimi.demo.shelldemo.service.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author suimi
 * @date 2019/1/18
 */
@RestController public class MathController {

    @Autowired private MathService mathService;

    @GetMapping("add/{a}/{b}") public int add(@PathVariable int a, @PathVariable int b) {
        return mathService.add(a, b);
    }

    @GetMapping("reduce/{a}/{b}") public int reduce(@PathVariable int a, @PathVariable int b) {
        return mathService.reduce(a, b);
    }

    @GetMapping("mutl/{a}/{b}") public int mutl(@PathVariable int a, @PathVariable int b) {
        return mathService.mutl(a, b);
    }
}
