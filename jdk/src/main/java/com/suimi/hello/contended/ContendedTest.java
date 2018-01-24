/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.contended;


/**
 * @author suimi
 * @date 2018-01-18
 */
public class ContendedTest implements Runnable {
    public static int NUM_THREADS = 4; // change

    public final static long ITERATIONS = 500L * 1000L * 1000L;

    private final int arrayIndex;

    private static VolatileLong[] longs;

    public ContendedTest(final int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    /**
     * -XX:-RestrictContended
     * –XX:+PrintFieldLayout  --- 只是在调试版jdk有效
     *
     * @param args
     * @throws Exception
     */
    public static void main(final String[] args) throws Exception {
        Thread.sleep(10000);
        System.out.println("starting....");
        if (args.length == 1) {
            NUM_THREADS = Integer.parseInt(args[0]);
        }

        longs = new VolatileLong[NUM_THREADS];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new VolatileLong();
        }
        final long start = System.nanoTime();
        runTest();
        System.out.println("duration = " + (System.nanoTime() - start));
    }

    private static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new ContendedTest(i));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }

    public void run() {
        long i = ITERATIONS + 1;
        while (0 != --i) {
            longs[arrayIndex].value = i;
        }
    }
}
