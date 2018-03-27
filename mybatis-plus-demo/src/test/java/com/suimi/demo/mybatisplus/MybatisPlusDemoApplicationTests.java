package com.suimi.demo.mybatisplus;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.plugins.Page;
import com.suimi.demo.mybatisplus.entity.PhoneEnum;
import com.suimi.demo.mybatisplus.entity.User;
import com.suimi.demo.mybatisplus.service.UserService;
import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("local")
@Slf4j
public class MybatisPlusDemoApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    public void test() {
        User user = User.builder().userName("test name").phone(PhoneEnum.CMCC).build();

        userService.insert(user);
        user = User.builder().userName("test name2").phone(PhoneEnum.CT).build();
        userService.insert(user);
        user = User.builder().userName("name").phone(PhoneEnum.CMCC).build();
        userService.insert(user);

        List<User> test = userService.findByName("test");
        for (User user1 : test) {
            log.info("user: {}", user1);
        }

        Page<User> page = new Page<>(0, 1, "id");
        Page<User> userPage = userService.findByName(page, "test");
        for (User user1 : userPage.getRecords()) {
            log.info("user: {}", user1);
        }
    }

}
