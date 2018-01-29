/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author suimi
 * @date 2018-01-29
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

    @Override
    protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
        return new CustomRequestHandlerMapping();
    }
}
