package com.gz.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ConsumerTest {
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
//        def();
//        directExchange();
        topicExchange();
//        fanoutExchange();
    }

    private static void fanoutExchange() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = RabbitUtils.connectionFactory("/oa");
        Connection conn = connectionFactory.newConnection();
        Channel channel = conn.createChannel();

        String exchangeName = "oa.fanout.exchange";
        String exangeType = "fanout";
        String routingKey = "";
        String queueName = "oa.task";

        channel.exchangeDeclare(exchangeName, exangeType, true, false, null);

        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        DefaultConsumer consumer = new ConsumerData(channel);
        channel.basicConsume(queueName, true, consumer);

//        Thread.sleep(Integer.MAX_VALUE);
//
//        channel.close();
//        conn.close();

    }

    private static void topicExchange() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = RabbitUtils.connectionFactory("/oa");
        Connection conn = connectionFactory.newConnection();
        Channel channel = conn.createChannel();

        String exchangeName = "oa2.topic.exchange";
        String exchangeType = "topic";
        String routingKey = "oa2.task.add.#";
//        String routingKey = "oa.task.add.*";
        String queueName = "oa2.task";

        //死信队列
        String dlxExchangeName = "oa2.dlx.exchange";
        String dlxQueueName = "oa2.dlx.queue";

        //正常队列绑定死信队列
        Map<String, Object> queueArgs = new HashMap<>();
        queueArgs.put("x-dead-letter-exchange", dlxExchangeName);
        queueArgs.put("x-max-length", 100);

        channel.exchangeDeclare(exchangeName, exchangeType, true, false, null);
        channel.queueDeclare(queueName, true, false, false, queueArgs);
        channel.queueBind(queueName, exchangeName, routingKey);

        //加载、绑定死信队列
        channel.exchangeDeclare(dlxExchangeName, exchangeType, true, false, null);
        channel.queueDeclare(dlxQueueName, true, false, false, null);
        channel.queueBind(dlxQueueName,dlxExchangeName,"#");

//        channel.basicQos(0, 1, false);

        channel.basicConsume(queueName, false,  new ConsumerData(channel));

//        Thread.sleep(Integer.MAX_VALUE);
//
//        channel.close();
//        conn.close();

    }

    private static void directExchange() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = RabbitUtils.connectionFactory("/oa");
        Connection conn = connectionFactory.newConnection();
        Channel channel = conn.createChannel();

        String exchangeName = "car.direct.exchange";
        String exangeType = "direct";
        String routingKey = "car.task.add";
        String queueName = "car.task";

        channel.exchangeDeclare(exchangeName, exangeType, true, false, null);

        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);

        DefaultConsumer consumer = new ConsumerData(channel);
        channel.basicConsume(queueName, true, consumer);

        Thread.sleep(Integer.MAX_VALUE);

        channel.close();
        conn.close();

    }

    public static void def() throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = RabbitUtils.connectionFactory("/oa");

        Connection conn = connectionFactory.newConnection();
        Channel channel = conn.createChannel();

        String queueName = "task-queue-01";
        channel.queueDeclare(queueName, true, false, false, null);

        DefaultConsumer consumer = new ConsumerData(channel);
        channel.basicConsume(queueName, true, consumer);


        Thread.sleep(1000);

        channel.close();
        conn.close();
    }
}
