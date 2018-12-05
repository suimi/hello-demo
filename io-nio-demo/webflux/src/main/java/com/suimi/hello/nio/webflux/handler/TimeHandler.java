/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.nio.webflux.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author suimi
 * @date 2018/11/29
 */
@Slf4j @Component public class TimeHandler {

    public Mono<ServerResponse> getTime(ServerRequest serverRequest) {

        return ok().contentType(MediaType.TEXT_PLAIN)
            .body(Mono.just("Now is " + new SimpleDateFormat("HH:mm:ss").format(new Date())), String.class);
    }

    public Mono<ServerResponse> getDate(ServerRequest serverRequest) {
        return ok().contentType(MediaType.TEXT_PLAIN)
            .body(Mono.just("Today is " + new SimpleDateFormat("yyyy-MM-dd").format(new Date())), String.class);
    }

    public Mono<ServerResponse> sendTimePerSec(ServerRequest serverRequest) {
        //MediaType.TEXT_EVENT_STREAM表示Content-Type为text/event-stream，即SSE：服务端推送（Server Send Event），在客户端发起一次请求后会保持该连接，服务器端基于该连接持续向客户端发送数据，从HTML5开始加入
        return ok().contentType(MediaType.TEXT_EVENT_STREAM).body(  // 1
            Flux.interval(Duration.ofSeconds(1)).   // 2
                map(l -> new SimpleDateFormat("HH:mm:ss").format(new Date())).log().take(10), String.class);
    }
}
