package com.suimi.demo.zeromq.publisher.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author suimi
 * @date 2019/6/4
 */
@Slf4j @Component public class Publisher {

    private int mode = ZMQ.PUB;

    private ZMQ.Context context = ZMQ.context(1);

    private ZMQ.Socket socket = context.socket(mode);

    private List<String> topics = new ArrayList<>();


    @PostConstruct public void init() {
        socket.bind("tcp://*:5555");
        topics.add("time");
        topics.add("top");
    }

    public boolean sendAll(String msg) {
        topics.stream().forEach(t -> this.send(t, msg));
        return true;
    }

    public boolean send(String topic, String msg) {
        String format = String.format("%s %s", topic, msg);
        log.info("send:{}", format);
        return socket.send(format);
    }

    public void registeTopic(String topic) {
        topics.add(topic);
    }

    public void removeTopic(String topic) {
        topics.remove(topic);
    }

}
