package com.gz.cloud.feign;

import com.gz.nacos.client.OAFallBack;
import com.gz.nacos.client.OAFallBackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "oa-product", fallbackFactory = OAFallBackFactory.class)
//@FeignClient(value = "oa-product", fallback = OAFallBack.class)
public interface FollowApi {
    @GetMapping(value = "/echo/{string}")
    String echo(@PathVariable String string);

    @GetMapping(value = "/echo3/{string}")
    String echo3(@PathVariable String string);
}
