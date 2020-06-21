package com.gz.dubbo.code;

import com.gz.dubbo.User;
import com.gz.dubbo.UserService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.RpcContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class DubboClient {
    public static void main(String[] args) throws InterruptedException, IOException {
        ApplicationConfig applicationConfig = new ApplicationConfig("app-oa-client");

//        RegistryConfig registryConfig = new RegistryConfig("multicast://224.1.1.1:3333");
        RegistryConfig registryConfig = new RegistryConfig("zookeeper://127.0.0.1:2181");
//        RegistryConfig registryConfig = new RegistryConfig("redis://127.0.0.1:6380");


        ReferenceConfig<UserService> referenceConfig = new ReferenceConfig<>();

        referenceConfig.setRegistry(registryConfig);
//        referenceConfig.setUrl("dubbo://127.0.1.1:20880/com.gz.dubbo.UserService?anyhost=true&application=oa-dubbo-test&deprecated=false&dubbo=2.0.2&dynamic=true&generic=false&group=oa-follow&interface=com.gz.dubbo.UserService&methods=getUser&pid=409953&release=2.7.7&retries=3&side=provider&threadpool=fixed&threads=5&timeout=5000&timestamp=1592640110278&token=fb8593cd-d7dc-4499-9eec-ea98ba39daf5&version=1");

        referenceConfig.setInterface(UserService.class);
//        referenceConfig.setUrl("dubbo://127.0.1.1:20880/com.gz.dubbo.UserService");
        referenceConfig.setApplication(applicationConfig);
        referenceConfig.setGroup("oa-v2");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line = bufferedReader.readLine();
            if (line.equals("quit")) {
                break;
            }

            try {
                RpcContext.getContext().setAttachment("a", "fff");

                if (line.startsWith("1")) {
                    UserService userService = referenceConfig.get();

                    userService.getUser(2L);
                    Future<User> future = RpcContext.getContext().getFuture();

                    userService.getUser(3L);
                    Future<User> future2 = RpcContext.getContext().getFuture();

                    userService.getUser(5L);
                    Future<User> future3 = RpcContext.getContext().getFuture();


                    User user = future.get(1, TimeUnit.MINUTES);
                    User user2 = future2.get(1, TimeUnit.MINUTES);
                    User user3 = future3.get(1, TimeUnit.MINUTES);

                    System.out.println(user);
                    System.out.println(user2);
                    System.out.println(user3);
                }

                if (line.startsWith("2")) {
                    List<User> list = referenceConfig.get().findUser(line.split(" ")[1], Integer.parseInt(line.split(" ")[2]));
                    System.out.println(list);
                }

            } catch (Throwable e) {
                e.printStackTrace();
            }


        }





    }
}
