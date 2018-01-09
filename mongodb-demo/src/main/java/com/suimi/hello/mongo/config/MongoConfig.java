/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.mongo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author suimi
 * @date 2018-01-09
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.suimi.hello.mongo.repository")
public class MongoConfig {
}
