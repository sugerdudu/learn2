package com.gz.dubbo.springboot.client;

import com.gz.dubbo.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableDubbo
@SpringBootApplication
public class AppDubboClient {
    @DubboReference
    UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(AppDubboClient.class, args);
    }

    @Bean
    public ApplicationRunner getBean() {
        return args -> {
            System.out.println(userService.getUser(12L));

        };
    }
}
