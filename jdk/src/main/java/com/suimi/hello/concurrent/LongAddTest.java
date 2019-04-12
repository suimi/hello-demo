package com.suimi.hello.concurrent;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author suimi
 * @date 2019/4/4
 */
public class LongAddTest {

    public static void main(String[] args) {
        LongAdder adder = new LongAdder();
        int i = 0;
        while (i++ < 1000) {
            new Thread(() -> {
                adder.increment();
                System.out.println(" sum = " + adder.sum());
            }).start();
        }

        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        System.out.println("adder = " + adder.sum());
    }
}
