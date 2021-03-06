package com.gz.nacos.product;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude ={ DubboAutoConfiguration.class, RedisAutoConfiguration.class})
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
            Thread.sleep(1000);

            return string;
        }

        @GetMapping(value = "/echo2/{string}")
        public String echo2(@PathVariable String string) throws InterruptedException {
            Thread.sleep(100);
            return string;
        }

        @GetMapping(value = "/echo3/{string}")
        public String echo3(@PathVariable String string) throws InterruptedException {
            System.out.println("echo3 " + string);
            //Thread.sleep(100);
            return string;
        }

        @GetMapping(value = "/oa-test/echo3/{string}")
        public String oaTestEcho3(@PathVariable String string) throws InterruptedException {
            System.out.println("oaTestEcho3 " + string);
            //Thread.sleep(100);
            return string;
        }

        @GetMapping(value = "/oa-test/echo4/{string}")
        public String oaTestEcho4(@PathVariable String string, @RequestParam("aaa") String aaa) throws InterruptedException {
            String str = "oaTestEcho3 " + string + " aaa "+aaa;
            System.out.println(str);
            //Thread.sleep(100);
            return str;
        }

        @GetMapping(value = "/oa-test/echo5/{string}")
        public String oaTestEcho5(@PathVariable String string,
                                  @RequestParam("aaa") String aaa,@RequestHeader("test-token") String testToken) throws InterruptedException {
            String str = "oaTestEcho3 " + string + " aaa " + aaa
                    + " ,  testToken:" + testToken;

            System.out.println(str);
            //Thread.sleep(100);
            return str;
        }
    }
}
