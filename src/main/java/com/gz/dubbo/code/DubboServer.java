package com.gz.dubbo.code;

import com.gz.dubbo.MockService;
import com.gz.dubbo.UserService;
import com.gz.dubbo.UserServiceImpl;
import org.apache.dubbo.config.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DubboServer {
    public static void main(String[] args) throws IOException {
        ApplicationConfig applicationConfig = new ApplicationConfig("app-oa");
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setSerialization("fastjson");
        protocolConfig.setPort(-1);

//        RegistryConfig registryConfig = new RegistryConfig("multicast://224.1.1.1:3333");
        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
//        RegistryConfig registryConfig = new RegistryConfig("redis://127.0.0.1:6380");

        ServiceConfig<UserService> serviceConfig = new ServiceConfig<>();
        serviceConfig.setInterface(UserService.class);
        serviceConfig.setToken(true);

        serviceConfig.setTimeout(5000);

        serviceConfig.setRef(new UserServiceImpl());
        //模块接口实现，在团队开发过程中，对方未完成实现也可调用接口
        //setMock(serviceConfig);

        serviceConfig.setRegistry(registryConfig);
        serviceConfig.setProtocol(protocolConfig);
        serviceConfig.setGroup("oa-v2");

        setLoadBalance(serviceConfig);

        serviceConfig.setApplication(applicationConfig);

        serviceConfig.export();

        System.in.read();

    }

    public static void setLoadBalance(ServiceConfig serviceConfig) {
        //随机
//        serviceConfig.setLoadbalance("random");
//        serviceConfig.setWeight(200);

        //一致性hash
        serviceConfig.setLoadbalance("consistenthash");


        Map<String, String> param = new HashMap<>();
        //由哪几个参数进行一致性hash计算
        param.put("hash.arguments", "0,1");
        //虚拟节点数
        param.put("hash.nodes", "100");

        MethodConfig methodConfig = new MethodConfig();
        methodConfig.setName("findUser");
        methodConfig.setParameters(param);

        serviceConfig.setMethods(Arrays.asList(methodConfig));

    }

    public static void setMock(ServiceConfig serviceConfig){
        serviceConfig.setRef(new MockService());
    }
}
