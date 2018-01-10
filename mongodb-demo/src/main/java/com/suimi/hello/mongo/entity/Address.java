/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.mongo.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * @author suimi
 * @date 2018-01-09
 */
@Data
@Document(collection = "address")
@TypeAlias("addr") //指定document中_class的值
public class Address {

    @Id
    private String id;

    private String city;

    private String street;
}
