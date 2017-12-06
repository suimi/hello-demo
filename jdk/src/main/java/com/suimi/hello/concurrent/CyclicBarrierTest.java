/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.concurrent;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * @author suimi
 * @date 2017-12-04
 */
public class CyclicBarrierTest {

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(2,()->{
            System.out.println("CyclicBarrier 线程" + Thread.currentThread().getName() + "执行");
        });
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                System.out.println("线程" + Thread.currentThread().getName() + "正在等待");
                try {
                    barrier.await();
                    System.out.println("线程" + Thread.currentThread().getName() + "执行结束");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        System.out.println("结束");
    }
}
