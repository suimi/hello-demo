package com.suimi.demo.zeromq.subscriber.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author suimi
 * @date 2019/6/4
 */
@Slf4j @Component public class Subscriber {

    private int mode = ZMQ.SUB;

    private ZMQ.Context context = ZMQ.context(1);

    private ZMQ.Socket socket = context.socket(mode);

    @PostConstruct public void init() {
        socket.connect("tcp://127.0.0.1:5555");
        socket.subscribe("top".getBytes());
        socket.subscribe("time".getBytes());
        socket.setReceiveTimeOut(1000*3);
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.schedule(this::receiveMsg, 1, TimeUnit.SECONDS);
    }

    private void receiveMsg() {
        while (true) {
            String s = socket.recvStr();
            log.info("received: {}", s);
        }
    }

}
