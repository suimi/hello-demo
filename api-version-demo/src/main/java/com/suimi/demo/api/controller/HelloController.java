/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suimi.demo.api.annotation.ApiVersion;
import lombok.extern.slf4j.Slf4j;

/**
 * @author suimi
 * @date 2018-01-29
 */
@RestController
@Slf4j
@ApiVersion("5")
public class HelloController {

    @RequestMapping("hello")
    @ApiVersion("1")
    public String hello(String value) {
        log.info("haha1..........");
        return "hello 1 " + value;
    }

    @RequestMapping("hello")
    @ApiVersion("2")
    public String hello2(String value) {
        log.info("haha2.........");
        return "hello 2 " + value;
    }

    @RequestMapping("hello")
    @ApiVersion("5")
    public String hello5(String value) {
        log.info("haha5.........");
        return "hello 5 " + value;
    }

    @RequestMapping("test")
    public String test(String value) {
        log.info("test.........");
        return "hello test " + value;
    }

}
