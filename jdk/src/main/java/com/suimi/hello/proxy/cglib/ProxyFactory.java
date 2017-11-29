/*
 * Copyright (c) 2013-2015,  suimi
 * Created by lichengcai on 2017-08-29.
 */
package com.suimi.hello.proxy.cglib;

public class ProxyFactory {
    public static <T> T newInstance(Class<T> tClass) {
        return  new CglibProxyInterceptor().getProxy(tClass);
    }
}
