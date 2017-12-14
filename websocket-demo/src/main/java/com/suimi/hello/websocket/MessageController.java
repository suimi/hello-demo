/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.RestController;

import com.suimi.hello.websocket.session.SocketSession;
import com.suimi.hello.websocket.session.SocketSessionRepository;
import lombok.extern.slf4j.Slf4j;

/**
 * @author suimi
 * @date 2017-12-12
 */
@RestController
@Slf4j
public class MessageController {

    @Autowired
    private SocketSessionRepository repository;

    @Autowired
    public SimpMessagingTemplate template;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(Message message) throws Exception {
        //        Thread.sleep(1000); // simulated delay
        if (log.isDebugEnabled()) {
            log.debug("received message:{}", message);
        }
        SocketSession socketSession = new SocketSession();
        socketSession.setSocketId(message.getIndex());
        repository.save(socketSession);
        return new Greeting("Hello, " + message.toString() + "!");
    }

    @MessageMapping("/oneToOne")
    public void oneToOne(Message message) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("received oneToOne message:{}", message);
        }
        SocketSession socketSession = new SocketSession();
        socketSession.setSocketId(message.getIndex());
        repository.save(socketSession);
        template.convertAndSendToUser("" + message.getIndex(), "/oneToOne",
                new Greeting("Hello, " + message.toString() + "!"));
    }
}
