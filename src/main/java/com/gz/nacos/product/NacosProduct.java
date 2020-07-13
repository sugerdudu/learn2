package com.gz.nacos.product;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = DubboAutoConfiguration.class)
@EnableDiscoveryClient
public class NacosProduct {


    public static void main(String[] args) {
        SpringApplication.run(NacosProduct.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }

    @RestController
    class EchoController {



        @GetMapping(value = "/echo/{string}")
        public String echo(@PathVariable String string, @RequestHeader("token") String token) throws InterruptedException {
            System.out.println(string);
            System.out.println("token = " + token);
            Thread.sleep(3000);

            return string;
        }
    }
}
