package com.suimi.hello.mongo;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.test.context.junit4.SpringRunner;

import com.suimi.hello.mongo.entity.Address;
import com.suimi.hello.mongo.entity.Person;
import com.suimi.hello.mongo.repository.AddressRepository;
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

    @Autowired
    private AddressRepository addressRepository;

    @Test
    public void contextLoads() {
    }

    @Test
    public void save() {
        for (int i = 1; i < 2; i++) {
            Person person = new Person();
            person.setName("test " + i);
            person.setPosition(new double[]{i, i});
            person.setAddresses(address());
            Person result = personService.save(person);
            log.info("result:{}", result);
        }
    }


    private Set<Address> address() {
        String[] citys = {"北京", "上海", "深圳", "成都"};
        String[] streets = {"天府一街", "天府二街", "天府三街", "天府四街", "天府五街"};
        Random random = new Random();
        Set<Address> adds = new HashSet<>();
        for (int i = 0, j = random.nextInt(3); i <= j; i++) {
            Address address = new Address();
            address.setCity(citys[random.nextInt(4)]);
            address.setStreet(streets[random.nextInt(5)]);
            adds.add(address);
        }
        addressRepository.save(adds);
        return adds;
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
