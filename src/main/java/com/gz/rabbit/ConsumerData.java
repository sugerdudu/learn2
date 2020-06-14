package com.gz.rabbit;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class ConsumerData extends DefaultConsumer {
    /**
     * Constructs a new instance and records its association to the passed-in channel.
     *
     * @param channel the channel to which this consumer is attached
     */
    public ConsumerData(Channel channel) {
        super(channel);
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
//        super.handleDelivery(consumerTag, envelope, properties, body);
        System.out.println("消费：" + new String(body) + " , " + "getDeliveryTag：" + envelope.getDeliveryTag());
//        System.out.println("getDeliveryTag：" + envelope.getDeliveryTag());
//        getChannel().basicAck(envelope.getDeliveryTag(),false);//确认消息接收，返回一个确认应答ACK
//        getChannel().basicNack(envelope.getDeliveryTag(), false, true);//拒绝确认应答ACK，再次重复消费
//        try {
//            Thread.sleep(300);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        //拒绝确认应答ACK，只消费一次
        getChannel().basicNack(envelope.getDeliveryTag(), false, false);


    }
}
