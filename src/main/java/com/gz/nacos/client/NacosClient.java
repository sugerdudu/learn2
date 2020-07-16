package com.gz.nacos.client;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
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
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication(exclude ={ DubboAutoConfiguration.class, RedisAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.gz.cloud.feign")
public class NacosClient {

    public static void main(String[] args) {
        SpringApplication.run(NacosClient.class, args);
    }


    @LoadBalanced

    //ribbon整合@SentinelRestTemplate
    @SentinelRestTemplate(blockHandler = "blockHandler",blockHandlerClass = BlockHandlerHelper.class
    ,fallback = "fallback",fallbackClass = BlockHandlerHelper.class )
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


    @Bean
    public SentinelResourceAspect sentinelResourceAspect(){
        return new SentinelResourceAspect();
    }

    //sentinel
    @PostConstruct
    public void init(){
        System.out.println("init");
        List<FlowRule> ruleList = new ArrayList<>();
        FlowRule flowRule = new FlowRule();
        flowRule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule.setResource("addFollow");
        flowRule.setCount(1);
//        flowRule.setMaxQueueingTimeMs(1000);

        ruleList.add(flowRule);


        FlowRule flowRule2 = new FlowRule();
        flowRule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
        flowRule2.setResource("addFollow2");
        flowRule2.setCount(3);
//        flowRule2.setMaxQueueingTimeMs(1000);

        ruleList.add(flowRule);
        ruleList.add(flowRule2);

        FlowRuleManager.loadRules(ruleList);
    }





    public static class SentinelBlock{
        public static String runLK(String string,BlockException b){
            return "流控3 "+string+"  " + b.toString();
        }
    }
}
