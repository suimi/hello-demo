/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.nio.webflux;

import org.junit.Test;
import org.springframework.util.StopWatch;

/**
 * @author suimi
 * @date 2018/12/18
 */
public class StopwatchTest {

    @Test
    public void test() throws InterruptedException {

        StopWatch sw = new StopWatch();
        sw.start("起床");
        Thread.sleep(1000);
        sw.stop();

        sw.start("洗漱");
        Thread.sleep(2000);
        sw.stop();

        sw.start("锁门");
        Thread.sleep(500);
        sw.stop();

        sw.start("上班");
        Thread.sleep(4000);
        sw.stop();

        System.out.println(sw.prettyPrint());
        System.out.println(sw.getTotalTimeMillis());
        System.out.println(sw.getTaskCount());
    }
}
