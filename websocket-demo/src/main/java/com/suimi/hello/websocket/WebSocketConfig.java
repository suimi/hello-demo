/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * @author suimi
 * @date 2017-12-12
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    public void configureMessageBroker(MessageBrokerRegistry config) {
        //设置服务器在topic和user这两个域上可以向客户端发消息；
        config.enableSimpleBroker("/topic","/user");
        //设置客户端发布消息的基础路径,客户端向服务端发送时的主题上面需要加"/app"作为前缀；
        config.setApplicationDestinationPrefixes("/app");
        //这句表示给指定用户发送（一对一）的主题前缀是“/user/”
        config.setUserDestinationPrefix("/user/");
        //可以已“.”来分割路径，看看类级别的@messageMapping和方法级别的@messageMapping
        //        config.setPathMatcher(new AntPathMatcher("."));
    }


    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //websocket就是websocket的端点，客户端需要注册这个端点进行链接，withSockJS允许客户端利用sockjs进行浏览器兼容性处理
        registry.addEndpoint("/websocket").withSockJS();
    }
}
