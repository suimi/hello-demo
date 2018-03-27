/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.jdk8.default_method;

public interface Test2 {
    default void method() {
        System.out.println("Test2");
    }
}
