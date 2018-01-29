/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.api.config;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.suimi.demo.api.annotation.ApiVersion;
import com.suimi.demo.api.annotation.ApiVersionCondition;
import com.suimi.demo.api.annotation.ServiceVersion;

/**
 * @author suimi
 * @date 2018-01-29
 */
public class CustomRequestHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestCondition<ApiVersionCondition> getCustomTypeCondition(Class<?> handlerType) {
        ServiceVersion serviceVersion = AnnotationUtils.findAnnotation(handlerType, ServiceVersion.class);
        return createCondition(serviceVersion);
    }

    @Override
    protected RequestCondition<ApiVersionCondition> getCustomMethodCondition(Method method) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        return createCondition(apiVersion);
    }

    private RequestCondition<ApiVersionCondition> createCondition(ApiVersion apiVersion) {
        return apiVersion == null ? null : new ApiVersionCondition(0, apiVersion.value());
    }

    private RequestCondition<ApiVersionCondition> createCondition(ServiceVersion apiVersion) {
        return apiVersion == null ? null : new ApiVersionCondition(apiVersion.value(), 0);
    }

}
