/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.tomcatio.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author suimi
 * @date 2018/12/4
 */
@RestController
public class DateTimeController {

    @GetMapping("/time")
    public String time() {
        return new SimpleDateFormat("HH:mm:ss").format(new Date());
    }

    @GetMapping("/date")
    public String date() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }
}
