/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.webflux.controller;

import com.suimi.hello.webflux.exception.ResourceNotFoundException;
import com.suimi.hello.webflux.service.UserService;
import com.suimi.hello.webflux.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author suimi
 * @date 2018/11/28
 */
@Slf4j
@RestController @RequestMapping("/user") public class UserController {

    private final UserService userService;

    @Autowired public UserController(final UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource not found")
    @ExceptionHandler(ResourceNotFoundException.class) public void notFound() {
    }

    @GetMapping("") public Flux<User> list() {
        return this.userService.list();
    }

    @GetMapping("/{id}") public Mono<User> getById(@PathVariable("id") final String id) {
        return this.userService.getById(id);
    }

    @PostMapping("") public Mono<User> create(@RequestBody final User user) {
        return this.userService.createOrUpdate(user);
    }

    @PutMapping("/{id}") public Mono<User> update(@PathVariable("id") final String id, @RequestBody final User user) {
        Objects.requireNonNull(user);
        user.setId(id);
        return this.userService.createOrUpdate(user);
    }

    @DeleteMapping("/{id}") public Mono<User> delete(@PathVariable("id") final String id) {
        return this.userService.delete(id);
    }

    @PostMapping(path = "/post", consumes = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<Void> post(@RequestBody Flux<User> users) {
        Flux<User> userFlux = users.log().doOnNext(userService::createOrUpdate);
        return userFlux.then();
    }

    @GetMapping(path = "/list", produces = MediaType.APPLICATION_STREAM_JSON_VALUE) public Flux<User> getUsers() {  // 2
        return userService.list();
    }
}
