/*
 * Copyright (c) 2013-2015,  suimi
 * Created by lichengcai on 2017-08-29.
 */
package com.suimi.hello.proxy.cglib;

import com.suimi.hello.proxy.ClassA;
import com.suimi.hello.proxy.ClassB;
import com.suimi.hello.proxy.jdk.AbstractClass;

public class CglibProxy {


    public static void main(String[] args) {
        AbstractClass a1 = ProxyFactory.newInstance(ClassA.class);
        a1.show();

        AbstractClass a2 = ProxyFactory.newInstance(ClassB.class);
        a2.show();
    }
}
