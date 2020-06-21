package com.gz.dubbo.springboot.server;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubbo
@SpringBootApplication
public class AppDubboServer {
    public static void main(String[] args) {
        SpringApplication.run(AppDubboServer.class, args);
    }
}
