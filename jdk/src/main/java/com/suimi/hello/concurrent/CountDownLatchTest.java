/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * @author suimi
 * @date 2017-12-04
 */
public class CountDownLatchTest {


    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                System.out.println("线程" + Thread.currentThread().getName() + "正在执行");
                latch.countDown();
            }).start();
        }
        System.out.println("等待结束");
        try {
            latch.await();
            System.out.println("结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
