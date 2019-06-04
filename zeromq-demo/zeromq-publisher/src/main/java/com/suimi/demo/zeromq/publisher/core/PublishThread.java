package com.suimi.demo.zeromq.publisher.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author suimi
 * @date 2019/6/4
 */
@Component public class PublishThread {

    @Autowired private Publisher publisher;

    @PostConstruct public void init() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        AtomicLong al = new AtomicLong(0);
        executor.scheduleAtFixedRate(() -> publisher.sendAll("" + al.incrementAndGet()), 0, 1, TimeUnit.MICROSECONDS);
    }
}
