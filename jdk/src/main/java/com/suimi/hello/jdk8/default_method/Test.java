/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.jdk8.default_method;

/**
 * @author suimi
 * @date 2018-03-07
 */
public class Test implements Test1, Test2 {

    public static void main(String[] args) {
        Test test = new Test();
        test.method();
    }

    @Override
    public void method() {
        System.out.println("Test");
    }
}
