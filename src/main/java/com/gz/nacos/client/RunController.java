package com.gz.nacos.client;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.gz.cloud.feign.FollowApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RefreshScope
@RestController
public class RunController {

    @Value("${oastatus.str}")
    String str;

    @Autowired
    TestService testService;


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

    public RunController() {
        System.out.println("---- RunController");
    }

    @GetMapping(value = "/test-a")
    public String testA() {
        return this.testService.common();
    }

    @GetMapping(value = "/test-b")
    public String testB() {
        return this.testService.common();
    }


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

        String val = restTemplate.getForObject(url + "/echo2/run2?token=123", String.class);

        System.out.println(val);
        return val;
    }

    //feign
    @GetMapping(value = "/run33/{string}")
    public String run33(@PathVariable String string) {
        System.out.println("str = " + str);
        String val = followApi.echo3(string + " - run33 feign" + oaConf.getStr());
        System.out.println(val);
        return val;
    }

    //feign
    @GetMapping(value = "/run3/{string}")
    public String run3(@PathVariable String string) {
        System.out.println("str = " + str);
        String val = followApi.echo(string + " - feign " + oaConf.getStr());
        System.out.println(val);
        return val;
    }

    //sentinal
    @GetMapping(value = "/run4/{string}")
    public String run4(@PathVariable String string) {
        System.out.println("str = " + str);

        Entry entry = null;
        try {
            entry = SphU.entry("addFollow");
            String val = followApi.echo(string + " - feign " + oaConf.getStr());
//                Thread.sleep(1000);
            System.out.println(val);
            return val;
        } catch (BlockException e) {
            System.out.println(e.getMessage());
            return "被流控 " + e.getMessage();
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }
    }

    @GetMapping(value = "/run5/{string}")
    @SentinelResource(value = "addFollow", blockHandler = "run5LK")
    public String run5(@PathVariable String string) {
        String val = followApi.echo(string + " - feign " + oaConf.getStr());
        System.out.println(val);
        return val;
    }

    public String run5LK(String string, BlockException b) {
        return "流控2 " + string + "  " + b.toString();
    }

    @GetMapping(value = "/run6/{string}")
    @SentinelResource(value = "addFollow", blockHandler = "runLK", blockHandlerClass = NacosClient.SentinelBlock.class)
    public String run6(@PathVariable String string) {
        String val = followApi.echo(string + " - feign " + oaConf.getStr());
        System.out.println(val);
        return val;
    }
}

