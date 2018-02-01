/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.api.config;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.suimi.demo.api.annotation.ApiVersion;
import com.suimi.demo.api.annotation.ApiVersionCondition;

/**
 * @author suimi
 * @date 2018-01-29
 */
public class CustomRequestHandlerMapping extends RequestMappingHandlerMapping {

    private ApiVersionProperties versionProperties;

    public CustomRequestHandlerMapping(ApiVersionProperties versionProperties) {
        this.versionProperties = versionProperties;
    }


    @Override
    protected RequestCondition<ApiVersionCondition> getCustomTypeCondition(Class<?> handlerType) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, ApiVersion.class);
        //如果类没有定义api版本，则设置为应用版本
        String apiVersionValue = apiVersion == null ? versionProperties.getVersion() : apiVersion.value();
        return createCondition(versionProperties.getVersion(), apiVersionValue);
    }

    @Override
    protected RequestCondition<ApiVersionCondition> getCustomMethodCondition(Method method) {
        ApiVersion apiVersion = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        //如果没有定义api版本，则设定为null，意为未定义
        return createCondition(versionProperties.getVersion(), apiVersion == null ? null : apiVersion.value());
    }

    private RequestCondition<ApiVersionCondition> createCondition(String currentVersion, String apiVersion) {

        return ApiVersionCondition.builder().paramName(versionProperties.getParamName()).currentVersion(currentVersion)
                                  .apiVersion(apiVersion).build();
    }

}
