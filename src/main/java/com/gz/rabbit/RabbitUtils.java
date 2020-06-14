package com.gz.rabbit;

import com.rabbitmq.client.ConnectionFactory;

public class RabbitUtils {
    public static ConnectionFactory connectionFactory(String virtualHost){
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("127.0.0.1");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setUsername("cgq");
        connectionFactory.setPassword("111111");
        connectionFactory.setConnectionTimeout(10000);
        return connectionFactory;
    }
}
