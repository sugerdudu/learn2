package com.gz.nacos.client;

import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.gz.cloud.feign.FollowApi;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Component
public class OAFallBackFactory implements FallbackFactory<FollowApi> {
    public OAFallBackFactory(){
        System.out.println("OAFallBackFactory --- ");
    }
    private String getResult(Throwable throwable) {
        if (throwable instanceof FlowException) {
            return "限流（FallBackFactory）";
        } else {
            return "降级（FallBackFactory）";
        }
    }

    @Override
    public FollowApi create(Throwable throwable) {
        System.out.println("FollowApi create");
        return new FollowApi() {
            @Override
            public String echo(String string) {
                return getResult(throwable) +" echo " + string;
            }

            @Override
            public String echo3(String string) {
                return getResult(throwable) +" echo3 " + string;
            }
        };
    }
}
