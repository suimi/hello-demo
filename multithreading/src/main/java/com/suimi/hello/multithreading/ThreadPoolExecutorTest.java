/*
 * Copyright (c) 2013-2015, 成都中联信通科技股份有限公司
 * Created by lichengcai on 2017-08-25.
 */
package com.suimi.hello.multithreading;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * 1.当线程池小于corePoolSize时，新提交任务将创建一个新线程执行任务，即使此时线程池中存在空闲线程。<br/>
 * 2.当线程池达到corePoolSize时，新提交任务将被放入workQueue中，等待线程池中任务调度执行<br/>
 * 3.当workQueue已满，且maximumPoolSize>corePoolSize时，新提交任务会创建新线程执行任务<br/>
 * 4.当提交任务数超过maximumPoolSize时，新提交任务由RejectedExecutionHandler处理<br/>
 * 5.当线程池中超过corePoolSize线程，空闲时间达到keepAliveTime时，关闭空闲线程<br/>
 * 6.当设置allowCoreThreadTimeOut(true)时，线程池中corePoolSize线程空闲时间达到keepAliveTime也将关闭<br/>
 */
public class ThreadPoolExecutorTest {


    private int value = 0;

    private void printValue() {
        try {
            Thread.currentThread().sleep(new Random().nextInt(100) * new Random().nextInt(50));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i = ++value;
        System.out.println(Thread.currentThread().getName() + ":" + i);
    }

    /**
     * 构造一个固定线程数目的线程池，配置的corePoolSize与maximumPoolSize大小相同，同时使用了一个无界LinkedBlockingQueue存放阻塞任务，因此多余的任务将存在再阻塞队列，不会由RejectedExecutionHandler处理
     */
    @Test
    public void test() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        for (int i = 0; i < 2000; i++) {
            executor.execute(() -> printValue());
        }
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构造一个固定线程数目的线程池，配置的corePoolSize与maximumPoolSize大小相同，同时使用了一个无界LinkedBlockingQueue存放阻塞任务，因此多余的任务将存在再阻塞队列，不会由RejectedExecutionHandler处理
     */
    @Test
    public void test1() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
                new SynchronousQueue<>());
        for (int i = 0; i < 1000; i++) {
            executor.execute(() -> printValue());
        }
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 构造一个只支持一个线程的线程池，配置corePoolSize=maximumPoolSize=1，无界阻塞队列LinkedBlockingQueue；保证任务由一个线程串行执行
     */
    @Test
    public void test2() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        for (int i = 0; i < 1000; i++) {
            executor.execute(() -> printValue());
        }
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
