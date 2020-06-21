package com.gz.dubbo;

import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MockService implements GenericService {
    @Override
    public Object $invoke(String method, String[] parameterTypes, Object[] args) throws GenericException {
        if (method.equals("getUser")) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", 12);
            map.put("name", "map mock");
            return map;
//            User user = new User();
//            user.setId(3L);
//            user.setName("mock data");
//            user.setAge(33);
//            return user;
        }
        return null;
    }

    @Override
    public CompletableFuture<Object> $invokeAsync(String method, String[] parameterTypes, Object[] args) throws GenericException {
        return null;
    }
}
