/*
 * Copyright (c) 2013-2015, 成都中联信通科技股份有限公司
 * Created by lichengcai on 2017-08-29.
 */
package com.suimi.hello.proxy;

import com.suimi.hello.proxy.jdk.AbstractClass;

public class ClassA implements AbstractClass {
    @Override
    public String show() {
        System.out.println("I'm A");
        return "is A";
    }
}
