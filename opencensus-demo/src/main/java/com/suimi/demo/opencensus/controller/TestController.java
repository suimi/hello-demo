/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.opencensus.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.opencensus.exporter.trace.logging.LoggingExporter;
import io.opencensus.exporter.trace.zipkin.ZipkinExporter;
import io.opencensus.trace.Span;
import io.opencensus.trace.Tracer;
import io.opencensus.trace.Tracing;
import io.opencensus.trace.samplers.Samplers;
import lombok.extern.slf4j.Slf4j;

/**
 * @author suimi
 * @date 2018-01-24
 */
@Slf4j
@RestController
public class TestController {

    private static final Tracer tracer = Tracing.getTracer();

    @GetMapping("test")
    public String test(String value) {

        LoggingExporter.register();

        Span span = tracer.spanBuilder("test").setRecordEvents(true).setSampler(Samplers.alwaysSample()).startSpan();
        span.addAnnotation("start log");
        log.info("test value:{}", value);
        span.addAnnotation("end log");
        tracer.withSpan(span);
        child();
        span.end();
        return value;
    }

    private void child() {
        Span childSpan = tracer.spanBuilder("child").startSpan();
        log.info("child");
        subChild();
        childSpan.addAnnotation("Annotation to the child Span");
        childSpan.end();
    }

    private void subChild() {
        Span childSpan = tracer.spanBuilder("sub child").startSpan();
        log.info("sub child");
        childSpan.end();
    }
}
