package com.gz.rocketmq.trans;

import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import javax.annotation.Resource;

@SpringBootApplication
public class ProducerTransTest implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ProducerTransTest.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        sendTrans();
    }

    @Resource
    RocketMQTemplate rocketMQTemplate;

    private void sendTrans() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Message<String> message = MessageBuilder.withPayload("trans message hello")
                    .setHeader(RocketMQHeaders.KEYS, "key_" + i).build();

            TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction(TransConfig.oaTransTopic, message, "a"+i);
            System.out.println(sendResult);
            Thread.sleep(100);
        }
    }
}
