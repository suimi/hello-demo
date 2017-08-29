/*
 * Copyright (c) 2013-2015, 成都中联信通科技股份有限公司
 * Created by lichengcai on 2017-08-29.
 */
package com.suimi.hello.proxy.jdk;

import java.lang.reflect.Proxy;

public class ProxyFactory {
    public static <T, O extends T> T newInstance(O ob) {
        return (T) Proxy.newProxyInstance(ob.getClass().getClassLoader(), ob.getClass().getInterfaces(), new ProxyInvokerHandler(ob));
    }
}
