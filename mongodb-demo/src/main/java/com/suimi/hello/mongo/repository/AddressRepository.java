/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.mongo.repository;

import java.util.List;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.suimi.hello.mongo.entity.Address;
import com.suimi.hello.mongo.entity.Person;

/**
 * @author suimi
 * @date 2018-01-09
 */

public interface AddressRepository extends MongoRepository<Address, String> {

}
