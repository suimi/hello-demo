/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.shelldemo.command;

import com.suimi.demo.shelldemo.service.MathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * @author suimi
 * @date 2019/1/18
 */
@ShellComponent public class MathCommand {

    @Autowired private MathService mathService;

    @ShellMethod(value = "add two integer", key = "sum", group = "Math")
    public int add(@ShellOption({"-a", "--a"}) int a, @ShellOption(value = {"-b", "--b"}, help = "b operate") int b) {
        return mathService.add(a, b);
    }

    @ShellMethod(value = "reduce two integer", group = "Math") public int reduce(@ShellOption({"-a", "--a"}) int a,
        @ShellOption(value = {"-b", "--b"}, help = "b operate") int b) {
        return mathService.reduce(a, b);
    }

    @ShellMethod("mutl two integer") public int mutl(int a, @ShellOption(defaultValue = ShellOption.NULL) Integer b) {
        return mathService.mutl(a, b == null ? 1 : b);
    }
}
