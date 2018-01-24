/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.disruptor.log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Service;

import com.lmax.disruptor.LiteBlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;
import com.suimi.demo.disruptor.log.handler.Log2DBHandler;
import com.suimi.demo.disruptor.log.handler.Log2MQHandler;
import com.suimi.demo.disruptor.log.handler.LogPrintHandler;

/**
 * @author suimi
 * @date 2018-01-15
 */
@Service
public class LogInfoDisruptor {

    int bufferSize = 1024;

    Disruptor<LogInfo> disruptor;

    @PostConstruct
    public void start() {
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        disruptor = new Disruptor<>(LogInfo::new, bufferSize, threadFactory, ProducerType.MULTI,
                new LiteBlockingWaitStrategy());
        EventHandlerGroup<LogInfo> printGroup = disruptor
                .handleEventsWithWorkerPool(new LogPrintHandler(), new Log2MQHandler());
        printGroup.then(new Log2DBHandler());
        disruptor.start();
    }

    public void publisher(CountDownLatch latch, LogInfo logInfo) {
        disruptor.publishEvent((event, sequence) -> {
            event.setLevel(logInfo.getLevel());
            event.setMessage(logInfo.getMessage());
        });
        latch.countDown();
    }

    @PreDestroy
    public void destory() throws InterruptedException {
        disruptor.shutdown();
    }
}
