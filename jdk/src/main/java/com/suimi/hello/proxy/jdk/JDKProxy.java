/*
 * Copyright (c) 2013-2015, 成都中联信通科技股份有限公司
 * Created by lichengcai on 2017-08-29.
 */
package com.suimi.hello.proxy.jdk;

import java.lang.reflect.Proxy;

import com.suimi.hello.proxy.ClassA;
import com.suimi.hello.proxy.ClassB;

public class JDKProxy {


    public static void main(String[] args) {
        //创建具体类ClassB的处理对象
        ProxyInvokerHandler handler = new ProxyInvokerHandler(new ClassA());
        //获得具体类ClassA的代理
        AbstractClass ac1 = (AbstractClass) Proxy
                .newProxyInstance(AbstractClass.class.getClassLoader(), new Class[]{AbstractClass.class}, handler);
        //调用ClassA的show方法。
        ac1.show();


        handler = new ProxyInvokerHandler(new ClassB());
        //获得具体类ClassB的代理
        AbstractClass ac2 = (AbstractClass) Proxy
                .newProxyInstance(AbstractClass.class.getClassLoader(), new Class[]{AbstractClass.class}, handler);
        //调用ClassB的show方法。
        ac2.show();
    }
}
