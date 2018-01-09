package com.suimi.hello.mongo;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.test.context.junit4.SpringRunner;

import com.suimi.hello.mongo.entity.Person;
import com.suimi.hello.mongo.repository.PersonRepository;
import com.suimi.hello.mongo.service.PersonService;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MongodbDemoApplicationTests {

    @Autowired
    private PersonService personService;

    @Autowired
    private PersonRepository repository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void save() {
        for (int i = 2; i < 100; i++) {
            Person person = new Person();
            person.setName("test " + i);
            person.setPosition(new double[]{i, i});
            Person result = personService.save(person);
            log.info("result:{}", result);
        }
    }

    @Test
    public void findAll() {
        List<Person> all = personService.findAll();
        log.info("all:{}", all);
    }

    @Test
    public void find() {
        Distance distance = new Distance(5, Metrics.NEUTRAL);
        List<Person> near = personService.findByPositionNear(new Point(50, 50), distance);
        log.info("near:{}", near);
    }

    @Test
    public void delete() {
        List<Person> byName = repository.findByName("test 1");
        repository.delete(byName);
        List<Person> byName1 = repository.findByName("test 1");
        assert byName1.size() == 0;
    }

    @Test
    public void clear() {
        repository.deleteAll();
    }

    @Test
    public void like() {
        Person person = new Person();
        person.setName("suimi");
        person.setPosition(new double[]{1, 1});
        person.setLikes(1);
        Person result = personService.save(person);
        log.info("result:{}", result);
        personService.like(result);
        Person likePerson = repository.findOne(result.getId());
        assert likePerson.getLikes() == person.getLikes() + 2;
    }


}
