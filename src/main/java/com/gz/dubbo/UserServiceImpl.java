package com.gz.dubbo;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public User getUser(Long id) {
        System.out.println(ManagementFactory.getRuntimeMXBean().getName());
        User user = new User();
        user.setId(id);
        user.setName("suger - "+ ManagementFactory.getRuntimeMXBean().getName());
        user.setAge(33);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> findUser(String name, Integer age) {
        System.out.println(ManagementFactory.getRuntimeMXBean().getName());
        User user = new User();
        user.setId(555L);
        user.setName(" findUser suger - "+ ManagementFactory.getRuntimeMXBean().getName());
        user.setAge(age);
        return Arrays.asList(user);
    }
}
