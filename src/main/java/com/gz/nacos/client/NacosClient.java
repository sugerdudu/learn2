package com.gz.nacos.client;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.config.annotation.NacosConfigurationProperties;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.gz.cloud.feign.FollowApi;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication(exclude = DubboAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.gz.cloud.feign")
public class NacosClient {

    public static void main(String[] args) {
        SpringApplication.run(NacosClient.class, args);
    }


    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new OkHttp3ClientHttpRequestFactory());
    }

    /**
     * 修改默认客户端负载均衡算法
     */
    @Bean
    public IRule iRule() {
        return new RandomRule();
    }

    @RefreshScope
    @RestController
    class RunController {

        @Value("${oastatus.str}")
        String str;


        @Autowired
        RestTemplate restTemplate;

        @Autowired
        private DiscoveryClient discoveryClient;

        @Autowired
        NacosDiscoveryProperties nacosDiscoveryProperties;

        @Autowired
        FollowApi followApi;

        @Autowired
        OAConf oaConf;




        @GetMapping(value = "/run/{string}")
        public String run(@PathVariable String string) {

            List<ServiceInstance> serviceInstances = discoveryClient.getInstances("oa-product");

            String url = serviceInstances.get(0).getUri().toString();
            System.out.println(url);
            String val = restTemplate.getForObject(url + "/echo/run", String.class);
            System.out.println(val);
            return string;
        }

        @GetMapping(value = "/run2/{string}")
        public String run2(@PathVariable String string) {
            String url = "http://oa-product";
            try {
                Instance instance =
                        nacosDiscoveryProperties.namingServiceInstance().selectOneHealthyInstance("oa-product", "follow");
                System.out.println(instance.toString());

            } catch (NacosException e) {
                e.printStackTrace();
            }
            String val = restTemplate.getForObject(url + "/echo/run2", String.class);

            System.out.println(val);
            return string;
        }

        @GetMapping(value = "/run3/{string}")
        public String run3(@PathVariable String string) {
            System.out.println("str = " + str);
            String val = followApi.echo(string + " - feign " + oaConf.getStr());
            System.out.println(val);
            return string;
        }
    }


}
