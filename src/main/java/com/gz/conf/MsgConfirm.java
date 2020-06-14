package com.gz.conf;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MsgConfirm implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("ack : " + ack + " , " + LocalDateTime.now().toString() + ", cause = " + cause +
                " , correlationData = " + correlationData);
    }
}
