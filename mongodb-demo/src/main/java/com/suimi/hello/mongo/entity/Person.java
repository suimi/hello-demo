/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.mongo.entity;


import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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

    @Indexed
    private String name;

    /**
     * 使用geoNear，需要建2d/2dsphere索引
     */
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2D)
    private double[] position;

    private int likes;

    /**
     * @DBRef 注解，属性对象存储在独立document,并建立映射关系，映射关系不支持级联存储,必须在save person之前，单独save address
     * 没有该注解则会存储在同一document
     */
    @DBRef
    @Field("adds") //mongodb 存储document时所有的属性名都会报存，所以为节约存储空间，尽量采用简写
    private Set<Address> addresses = new HashSet<Address>();

}
