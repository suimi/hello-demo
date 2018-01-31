/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.api;

import org.junit.Test;
import org.springframework.util.AntPathMatcher;

import com.suimi.demo.api.annotation.ApiVersionCondition;
import lombok.extern.slf4j.Slf4j;

import static com.sun.corba.se.impl.orbutil.ORBUtility.compareVersion;

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

    @Test
    public void versionCompare() {
        ApiVersionCondition condition = ApiVersionCondition.builder().build();
        assert condition.compareVersion("1.2.2.a", "1.2.2.b") < 0;
        assert condition.compareVersion("1.2.2", "1.2.1") > 0;
        assert condition.compareVersion("1.2.2", "1.2.2") == 0;
        assert condition.compareVersion("1.2.2", "1.2.3") < 0;
        assert condition.compareVersion("1.2.2", "1.2") > 0;
        assert condition.compareVersion("1.2.2", "1.2.2.1") < 0;
        assert condition.compareVersion("1.2.2", "1.2.2.snap") < 0;
        assert condition.compareVersion("1.2.2.a", "1.2.2.a") == 0;

    }


}
