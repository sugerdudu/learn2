package com.gz.nacos.client;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Component;

@Component
public class TestService {
    @SentinelResource("test-common")
    public String common(){
        return "common " + System.currentTimeMillis();
    }
}
