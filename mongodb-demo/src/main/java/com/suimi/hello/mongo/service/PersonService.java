/*
 * Copyright (c) 2013-2017, suimi
 */
package com.suimi.hello.mongo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.suimi.hello.mongo.entity.Person;
import com.suimi.hello.mongo.repository.PersonRepository;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author suimi
 * @date 2018-01-09
 */
@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MongoOperations mongoOperations;

    public Person save(Person person) {
        return personRepository.save(person);
    }

    public List<Person> findByPositionNear(Point point, Distance distance) {
        return personRepository.findByPositionNear(point, distance);
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public void like(Person person) {
        mongoOperations.updateMulti(query(where("id").is(person.getId())), new Update().inc("likes", 2), Person.class);
    }
}
