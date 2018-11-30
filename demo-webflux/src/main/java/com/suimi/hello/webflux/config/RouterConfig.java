/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.webflux.config;

import com.suimi.hello.webflux.handler.TimeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author suimi
 * @date 2018/11/29
 */
@Configuration public class RouterConfig {
    @Autowired
    private TimeHandler timeHandler;

    @Bean
    public RouterFunction<ServerResponse> timerRouter() {
        return route(GET("/time"), req -> timeHandler.getTime(req))
            .andRoute(GET("/date"), timeHandler::getDate)// 这种方式相对于上一行更加简洁
            .andRoute(GET("/times"),timeHandler::sendTimePerSec);
    }
}
