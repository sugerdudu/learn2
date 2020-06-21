package com.gz.dubbo.springboot.server;

import com.gz.dubbo.User;
import com.gz.dubbo.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;

@DubboService
@Component
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(Long id) {

        System.out.println(ManagementFactory.getRuntimeMXBean().getName());
        User user = new User();
        user.setId(id);
        user.setName("suger - "+ ManagementFactory.getRuntimeMXBean().getName());
        user.setAge(33);
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
