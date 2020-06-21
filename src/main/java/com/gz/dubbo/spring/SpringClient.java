package com.gz.dubbo.spring;

import com.gz.dubbo.User;
import com.gz.dubbo.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SpringClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");

        UserService userService = context.getBean(UserService.class);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            if (bufferedReader.readLine().equals("quit")) {
                break;
            }

            User user = userService.getUser(2L);
            System.out.println(user);
            Thread.sleep(1000);
        }
    }
}
