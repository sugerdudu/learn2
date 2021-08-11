package com.gz.dubbo.spring;

import com.alibaba.fastjson.JSONObject;
import com.boczj.CommonService.Service.RlmsInstallmentService;
import com.gz.dubbo.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class SpringClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("consumer.xml");

        UserService userService = context.getBean(UserService.class);
        RlmsInstallmentService rlmsInstallmentService = context.getBean(RlmsInstallmentService.class);
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("--------");
        System.out.println("user:"+ JSONObject.toJSONString(userService.getUser(2L)));
        System.out.println("rlmsInstallmentService:"+ JSONObject.toJSONString(rlmsInstallmentService.carInstallmentApplyA("1111111111")));
        System.out.println("========");

      /*  while (true) {
            if (bufferedReader.readLine().equals("quit")) {
                break;
            }

            User user = userService.getUser(2L);
            System.out.println("user:"+ JSONObject.toJSONString(user));
            Thread.sleep(1000);
        }*/
    }
}
