/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.concurrent;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * @author suimi
 * @date 2017-12-04
 */
public class SemaphoreTest {

    public static void main(String[] args) {
        Semaphore barrier = new Semaphore(2, true);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    System.out.println("线程" + Thread.currentThread().getName() + "申请资源");
                    barrier.acquire();
                    System.out.println("线程" + Thread.currentThread().getName() + "执行");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    barrier.release();
                }
            }).start();
        }
        System.out.println("结束");
    }

}
