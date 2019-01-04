/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.rxjava;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.testng.annotations.Test;

/**
 * @author suimi
 * @date 2018/12/11
 */
public class RxExample {

    @Test public void test1() {
        String[] names = new String[] {"A", "B", "cde ", " hello"};
        Observer observer = new Observer() {
            @Override public void onSubscribe(Disposable disposable) {
                System.out.println("on subscribe ...");
            }

            @Override public void onNext(Object o) {
                System.out.println("on next ...");
                System.out.println(o);
            }

            @Override public void onError(Throwable throwable) {
                System.out.println("on error ...");
            }

            @Override public void onComplete() {
                System.out.println("on complete ...");
            }
        };
        Observable.create(e -> {
            e.onNext("a");
            e.onNext("b");
            e.onNext("c");
            e.onError(new RuntimeException());
            e.onComplete();
            e.onNext("des");
        }).subscribe(observer);
    }
}
