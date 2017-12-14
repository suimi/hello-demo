/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.nio2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author suimi
 * @date 2017-12-14
 */
@RestController
public class Controller {

    @GetMapping("/hello")
    public String hello() {

        return "hello nio2";
    }
}
