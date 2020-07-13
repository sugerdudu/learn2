package com.gz.cloud.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "oa-product",configuration = FeignConfig.class)
public interface FollowApi {
    @GetMapping(value = "/echo/{string}")
    String echo(@PathVariable String string);
}
