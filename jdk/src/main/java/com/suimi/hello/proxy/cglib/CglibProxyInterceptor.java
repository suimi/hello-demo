/*
 * Copyright (c) 2013-2015, 成都中联信通科技股份有限公司
 * Created by lichengcai on 2017-08-29.
 */
package com.suimi.hello.proxy.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibProxyInterceptor implements MethodInterceptor {

    public <T> T getProxy(Class<T> tClass) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(tClass);
        enhancer.setCallback(this);
        return (T) enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("before invoke：" + methodProxy);
                Object result = methodProxy.invokeSuper(o, objects);
//        Object result = method.invoke(o, objects);
        System.out.println("after invoke:" + result);
        return result;
    }
}
