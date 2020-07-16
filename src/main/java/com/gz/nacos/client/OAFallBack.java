package com.gz.nacos.client;

import com.gz.cloud.feign.FollowApi;
import org.springframework.stereotype.Component;

@Component
public class OAFallBack implements FollowApi {
    @Override
    public String echo(String string) {
        return "流控（echo）";
    }

    @Override
    public String echo3(String string) {
        return "流控（echo3）";
    }
}
