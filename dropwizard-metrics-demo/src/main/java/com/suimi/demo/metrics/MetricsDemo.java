/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.metrics;

import com.codahale.metrics.*;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author suimi
 * @date 2019/1/4
 */
public class MetricsDemo {

    /**
     * 实例化一个registry，最核心的一个模块，相当于一个应用程序的metrics系统的容器，维护一个Map
     */
    private static final MetricRegistry registry = new MetricRegistry();

    private static Queue<Integer> queue = new LinkedBlockingDeque<>();

    /**
     * 在控制台上打印输出
     */
    private static final ConsoleReporter reporter = ConsoleReporter.forRegistry(registry).build();



    public static void main(String[] args) throws InterruptedException, ExecutionException {

        reporter.start(3, TimeUnit.SECONDS);

        //Gauges是一个最简单的计量，一般用来统计瞬时状态的数据信息
        registry.gauge(MetricRegistry.name(MetricsDemo.class, "queue", "size"), () -> () -> queue.size());
        //Counter是Gauge的一个特例，维护一个计数器，可以通过inc()和dec()方法对计数器做修改
        Counter counter = registry.counter(MetricRegistry.name(MetricsDemo.class, "queue", "counter"));
        //Meters用来度量某个时间段的平均处理次数（request per second），每1、5、15分钟的TPS，还有全部时间的速率（速率就是平均值）
        Meter requests = registry.meter(MetricRegistry.name(MetricsDemo.class, "queue","poll"));
        //Histograms主要使用来统计数据的分布情况，最大值、最小值、平均值、中位数，百分比（75%、90%、95%、98%、99%和99.9%）
        Histogram histogram = registry.histogram(MetricRegistry.name(MetricsDemo.class, "queue", "histogram"));
        //Timers主要是用来统计某一块代码段的执行时间以及其分布情况
        Timer timer = registry.timer(MetricRegistry.name(MetricsDemo.class, "queue", "timer"));

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        Random random = new Random();

        executorService.scheduleWithFixedDelay(() -> {
            Timer.Context time = timer.time();
            queue.poll();
            requests.mark();
            try {
                histogram.update(queue.size());

                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                time.stop();
            }
        }, 1000, 10, TimeUnit.MILLISECONDS);


        for (int i = 0; i < 10000; i++) {
            queue.add(i);
            counter.inc();
            Thread.sleep(random.nextInt(100));
        }


    }
}
