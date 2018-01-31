/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * @author suimi
 * @date 2018-01-30
 */
@Configuration
@ConfigurationProperties(prefix = "app.api")
@Data
public class VersionProperties {
    private int version = Integer.MAX_VALUE;
}
