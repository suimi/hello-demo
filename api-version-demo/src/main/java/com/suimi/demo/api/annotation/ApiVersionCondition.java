/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.api.annotation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author suimi
 * @date 2018-01-29
 */
@Getter
@ToString
@Builder
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {
    public final static int DEFAULT_SERVICE_VERSION = Integer.MAX_VALUE;

    /**
     * 当前版本
     */
    private int currentVersion;

    /**
     * api 版本
     */
    private int apiVersion;

    // 采用最后定义优先原则,则方法上的定义覆盖类上面的定义
    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {
        return ApiVersionCondition.builder().currentVersion(currentVersion)
                                  .apiVersion(other.apiVersion < 0 ? currentVersion : other.apiVersion).build();
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        String apiVersion = request.getHeader("api-version");
        if (StringUtils.hasLength(apiVersion)) {
            Integer version = Integer.valueOf(apiVersion);
            // 如果请求的版本号大于配置版本号， 则满足
            if (version >= this.apiVersion && version <= this.currentVersion) {
                return this;
            }
        } else {
            //如果没有版本信息，满足所有
            return this;
        }
        return null;
    }

    @Override
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        // 优先匹配最新的版本号
        return other.getApiVersion() - this.apiVersion;
    }

}
