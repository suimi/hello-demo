/*
 * Copyright (c) 2013-2015, 成都中联信通科技股份有限公司
 * Created by lichengcai on 2017-08-29.
 */
package com.suimi.hello.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理类使用到了一个接口InvocationHandler和一个代理类Proxy ，这两个类配合使用实现了动态代理的功能。
 * 那么什么是动态代理呢？
 * 我们平常说的代理类是指： 给每个具体类写一个代理类，以后要使用某个具体类时，只要创建它的代理类的对象，然后调用代理类的方法就可以了。
 * 可是如果现在有许多的具体类，那就需要有许多的代理类才可以，这样很显然不合适。所以动态代理就应运而生了，我们只要写一个类实现
 * InvocationHandler 并实现它的invoke方法，然后再用Proxy的工厂方法newProxyInstance（）创建一个代理对象，这个对象同样可以实现对具体类的代理功能。
 * 而且想代理哪个具体类，只要给Handler（以下代码中的Invoker）的构造器传入这个具体对象的实例就可以了。感觉是不是自己为该具体类造了一个代理类呢？呵呵~
 */
//动态代理类，实现InvocationHandler接口
public class ProxyInvokerHandler<T> implements InvocationHandler {

    T ac;

    public ProxyInvokerHandler(T ac) {
        this.ac = ac;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //        System.out.println("before invoke：" + proxy);
        Object invoke = method.invoke(ac, args);
        System.out.println("after invoke:" + invoke);
        return invoke;
    }
}
