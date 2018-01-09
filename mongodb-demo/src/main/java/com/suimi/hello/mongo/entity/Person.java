/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.mongo.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * @author suimi
 * @date 2018-01-09
 */
@Data
@Document(collection = "person")
public class Person {

    @Id
    private String id;

    private String name;

    private double[] position;

    private int likes;


}
