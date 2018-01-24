package com.suimi.demo.disruptor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.suimi.demo.disruptor.log.LogInfo;
import com.suimi.demo.disruptor.log.LogInfoDisruptor;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DisruptorDemoApplicationTests {

    @Autowired
    LogInfoDisruptor disruptor;

    @Test
    public void test() {
        int size = 10;
        CountDownLatch latch = new CountDownLatch(size);
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < size; i++) {
            final int j = i;
            executor.execute(() -> {
                LogInfo logInfo = new LogInfo(LogLevel.DEBUG, "message " + j + "");
                disruptor.publisher(latch, logInfo);
            });
        }


    }
}
