package com.gz.dubbo.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class SpringServer {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("provide.xml");
        System.in.read();
    }


}
