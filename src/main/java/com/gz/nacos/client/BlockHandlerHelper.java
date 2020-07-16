package com.gz.nacos.client;

import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;


public class BlockHandlerHelper {
    public static SentinelClientHttpResponse blockHandler(HttpRequest httpRequest, byte[] body
    , ClientHttpRequestExecution requestExecution, BlockException ex){
        try {
            return new SentinelClientHttpResponse(new ObjectMapper().writeValueAsString("流量被限制"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static SentinelClientHttpResponse fallback(HttpRequest httpRequest, byte[] body
    , ClientHttpRequestExecution requestExecution, BlockException ex){
        try {
            return new SentinelClientHttpResponse(new ObjectMapper().writeValueAsString("被降级"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
