package com.gz.rabbit;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class ProducterTest {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
//        def();
//        directExchange();
        topicExchange();
//        fanoutExchange();
    }

    private static void fanoutExchange() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = RabbitUtils.connectionFactory("/oa");
        Connection conn = connectionFactory.newConnection();
        Channel channel = conn.createChannel();

        String exchangeName = "oa.fanout.exchange";
        String exangeType = "topic";
//        String routingKey = "oa.task.add.ff121232424sdfdsf.sss.efef";
        String queueName = "oa.task";

        String message = "test fanout exchange oa";

//        channel.exchangeDeclare(exchangeName, exangeType, true, false, null);
//        channel.queueDeclare(queueName, true, false, false, null);
//        channel.queueBind(queueName, exchangeName, routingKey);


        channel.basicPublish(exchangeName, "oa.task.add", null, "message1".getBytes());
        channel.basicPublish(exchangeName, "oa.task.add.abc", null, "message2".getBytes());
        channel.basicPublish(exchangeName, "oa.task.add.ff3.sss.efef", null, "message3".getBytes());
        channel.close();
        conn.close();
    }

    private static void topicExchange() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = RabbitUtils.connectionFactory("/oa");
        Connection conn = connectionFactory.newConnection();
        Channel channel = conn.createChannel();
        String exchangeName = "oa2.topic.exchange";
        String exangeType = "topic";
//        String routingKey = "oa.task.add.ff121232424sdfdsf.sss.efef";
        String queueName = "oa2.task";

        String message = "test topic exchange oa";

//        channel.exchangeDeclare(exchangeName, exangeType, true, false, null);
//        channel.queueDeclare(queueName, true, false, false, null);
//        channel.queueBind(queueName, exchangeName, routingKey);
        Map<String, Object> headers = new HashMap<>();
        headers.put("name", "?????????1");
        headers.put("company", "????????????2");


        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties().builder()
                .deliveryMode(2)
                .expiration("10000")
                .contentEncoding("utf-8")
                .correlationId(UUID.randomUUID().toString())
                .headers(headers)
                .build();


        //????????????????????????mq?????????????????????
        channel.confirmSelect();
        channel.addConfirmListener(new ConfirmListener(){
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("handleAck : " + LocalDateTime.now().toString() + ", deliveryTag = " + deliveryTag +
                        " , multiple = " + multiple);
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("handleNack : " + LocalDateTime.now().toString() + ", deliveryTag = " + deliveryTag +
                        " , multiple = " + multiple);
            }
        });

        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("replyCode = " + replyCode + ", replyText = " + replyText + " , routingKey = " + routingKey + ",body = " + new String(body));
            }
        });

        String routingKey = "oa2.task.add.aaa";
        //???????????????????????????
        String errorRoutingKey = "oa2.task.add1.error";
//        channel.basicPublish(exchangeName, routingKey, basicProperties, ("message test").getBytes());
//        channel.basicPublish(exchangeName, routingKey, basicProperties, ("message test2").getBytes());
//        channel.basicPublish(exchangeName, routingKey, basicProperties, ("message test3").getBytes());

        for (int i = 0; i < 10; i++) {
            System.out.println("?????? "+i);
            channel.basicPublish(exchangeName, routingKey, basicProperties, ("message "+i).getBytes());
        }

        if(false) {
            //?????????????????????
            channel.basicPublish(exchangeName, errorRoutingKey, true, basicProperties, ("error message mandatory true").getBytes());
            channel.basicPublish(exchangeName, errorRoutingKey, false, basicProperties, ("error message mandatory false").getBytes());
        }
//        channel.basicPublish(exchangeName, "oa.task.add.abc", basicProperties, "message2".getBytes());
//        channel.basicPublish(exchangeName, "oa.task.add.ff3.sss.efef", basicProperties, "message3".getBytes());
//        channel.close();
//        conn.close();
    }

    private static void directExchange() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = RabbitUtils.connectionFactory("/oa");
        Connection conn = connectionFactory.newConnection();
        Channel channel = conn.createChannel();

        String exchangeName = "car.direct.exchange";
        String exangeType = "direct";
        String routingKey = "car.task.add1";
        String queueName = "car.task";

        String message = "test direct exchange shop";

//        channel.exchangeDeclare(exchangeName, exangeType, true, false, null);
//        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);


        channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
        channel.close();
        conn.close();
    }

    private static void def() throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = RabbitUtils.connectionFactory("/oa");

        Connection conn = connectionFactory.newConnection();
        Channel channel = conn.createChannel();


        for (int i = 0; i < 10; i++) {
            String message = "hello > " + i;
            channel.basicPublish("", "task-queue-01", null, message.getBytes());
        }

        channel.close();
        conn.close();
    }


}
