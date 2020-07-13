package com.gz.cloud.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class OAFeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        System.out.println("OAFeignInterceptor.apply");
        requestTemplate.header("token", "aaaaafefe");
    }
}
