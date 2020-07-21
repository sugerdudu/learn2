package com.gz;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.cloud.gateway.handler.predicate.GatewayPredicate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;


@Component
public class TestRoutePredicateFactory extends AbstractRoutePredicateFactory<TestConfig> {

    public TestRoutePredicateFactory() {
        super(TestConfig.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(TestConfig config) {
        System.out.println("TestRoutePredicateFactory : "+config.getName());
        return new GatewayPredicate() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                System.out.println("GatewayPredicate: test :" + serverWebExchange.getRequest().getURI());
               return true;
            }

            @Override
            public String toString() {
                return String.format("test name: %s", config.getName());
            }
        };
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("name");
    }
}
