/*
 * Copyright (c) 2013-2015,  suimi
 * Created by lichengcai on 2017-08-29.
 */
package com.suimi.hello.proxy.jdk;

import com.suimi.hello.proxy.ClassA;
import com.suimi.hello.proxy.ClassB;

public class JDKProxy2 {


    public static void main(String[] args) {
        AbstractClass a1 = ProxyFactory.newInstance(new ClassA());
        a1.show();

        AbstractClass a2 = ProxyFactory.newInstance(new ClassB());
        a2.show();
    }
}
