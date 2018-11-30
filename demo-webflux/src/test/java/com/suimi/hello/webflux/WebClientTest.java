/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.webflux;

import com.suimi.hello.webflux.vo.User;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author suimi
 * @date 2018/11/30
 */
public class WebClientTest {


    @Test
    public void webClientTest1() throws InterruptedException {
        WebClient webClient = WebClient.create("http://localhost:8080");   // 1
        Mono<String> resp = webClient
            .get().uri("/time") // 2
            .retrieve() // 异步地获取response信息；
            .bodyToMono(String.class);  // 4
        resp.subscribe(System.out::println);    // 5
        TimeUnit.SECONDS.sleep(1);  // 6
    }

    @Test
    public void webClientTest2() throws InterruptedException {
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build(); // 1
        webClient
            .get().uri("/times")
            .accept(MediaType.TEXT_EVENT_STREAM) // 2
            .exchange() // 获取response信息，返回值为ClientResponse，retrive()可以看做是exchange()方法的“快捷版”；
            .flatMapMany(t->t.bodyToFlux(String.class))
            .doOnNext(System.out::println)  // 5
            .blockLast();   // 6
    }

    @Test
    public void webClientTest3() throws InterruptedException {
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8080").build(); // 1
        webClient
            .get().uri("/times")
            .accept(MediaType.TEXT_EVENT_STREAM) // 2
            .retrieve() // 获取response信息，返回值为ClientResponse，retrive()可以看做是exchange()方法的“快捷版”；
            .bodyToFlux(String.class)
            .log()
            .blockLast();   // 6
    }

    @Test
    public void webClientTest4() {
        Flux<User> userFlux = Flux.interval(Duration.ofSeconds(1))
            .map(l -> new User("id_"+l,"name_"+l, 12)).take(10); // 1
        WebClient webClient = WebClient.create("http://localhost:8080");
        webClient
            .post().uri("/user/post")
            .contentType(MediaType.APPLICATION_STREAM_JSON) // 2
            .body(userFlux, User.class) // 3
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    @Test
    public void webClientTest5() {
        WebClient webClient = WebClient.create("http://localhost:8080");
        webClient
            .get().uri("/user/list")
            .accept(MediaType.APPLICATION_STREAM_JSON) // 2
            .retrieve()
            .bodyToFlux(User.class)
            .log()
            .blockLast();
    }
}
