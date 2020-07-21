package com.gz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AppGateWay {
    public static void main(String[] args) {
        SpringApplication.run(AppGateWay.class);
    }
}
