/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.demo.api.annotation;

import java.lang.annotation.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping
public @interface ApiVersion {

    String value();

}
