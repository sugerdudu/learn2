package com.gz;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

@Component
public class TestGlobalFilter implements GlobalFilter , Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("TestGlobalFilter.filter");


        ServerHttpRequest request = exchange.getRequest().mutate()
                .header("test-token", "aaa:test-token"
                        , String.valueOf(System.currentTimeMillis())).build();

        ServerWebExchange webExchange = exchange.mutate().request(request).build();

        return chain.filter(webExchange).then(new Mono<Void>() {
            @Override
            public void subscribe(CoreSubscriber<? super Void> actual) {
                System.out.println("TestGlobalFilter.subscribe "+actual.toString());
            }
        });
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
