/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.api;

import org.junit.Test;
import org.springframework.util.AntPathMatcher;

import lombok.extern.slf4j.Slf4j;

/**
 * @author suimi
 * @date 2018-01-30
 */
@Slf4j
public class SimpleTest {

    @Test
    public void test() {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        assert pathMatcher.match("/{version:(v\\d+)*}/hello", "/v5/hello");
        assert pathMatcher.match("/{version:(v\\d+)*}/hello", "/v56/hello");
        assert !pathMatcher.match("/{version:(v\\d+)*}/hello", "/vd/hello");
        assert pathMatcher.match("/(v\\d+)*/hello", "/hello");
    }
}
