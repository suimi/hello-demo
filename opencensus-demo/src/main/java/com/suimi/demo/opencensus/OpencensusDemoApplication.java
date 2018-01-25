package com.suimi.demo.opencensus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.opencensus.exporter.trace.logging.LoggingExporter;
import io.opencensus.exporter.trace.zipkin.ZipkinExporter;

@SpringBootApplication
public class OpencensusDemoApplication {

    public static void main(String[] args) {
        LoggingExporter.register();
        ZipkinExporter.createAndRegister("http://127.0.0.1:8883/api/v2/spans", "Test");
        SpringApplication.run(OpencensusDemoApplication.class, args);
    }
}
