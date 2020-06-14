package com.gz.rabbit.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class OAListener {
    @RabbitListener(queues = {"oa3.task"})
    @RabbitHandler
    public void show(Message message, Channel channel) throws IOException {
        System.out.println("-------- show ----------");

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.readValue(message.getBody(), Map.class);
        System.out.println(message.toString());
        System.out.println(map.toString());
    }
}
