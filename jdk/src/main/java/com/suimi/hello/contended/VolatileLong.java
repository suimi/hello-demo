/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.contended;

import sun.misc.Contended;

/**
 * @author suimi
 * @date 2018-01-18
 */
//@Contended
public class VolatileLong {
    public volatile long value = 0L;

    //volatile只能保证可见性，不能保证原子性
    public void increase() {
        value++;
    }

    //需要通过同步锁保证原子性
    public synchronized void increaseSync() {
        value++;
    }

    public static void main(String[] args) {
        System.out.println("Thread.activeCount() = " + Thread.activeCount());
        final VolatileLong test = new VolatileLong();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
//                    test.increase();
                    test.increaseSync();
                }

            }).start();
        }

        //IDEA运行 Thread.activeCount()会输出2
        while (Thread.activeCount() > 2)  //保证前面的线程都执行完
            Thread.yield();
        System.out.println(test.value);
    }
}
