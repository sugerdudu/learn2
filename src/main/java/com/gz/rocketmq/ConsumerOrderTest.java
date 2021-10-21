package com.gz.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 有序消费
 */
public class ConsumerOrderTest {
    public static void main(String[] args) throws MQClientException, InterruptedException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("oa-group2");
//        consumer.setNamesrvAddr("127.0.0.1:9876;127.0.0.1:9877");
        consumer.setNamesrvAddr("127.0.0.1:9876");
//        consumer.setMessageModel(MessageModel.BROADCASTING);
        String ex = "*";
        consumer.subscribe("follow-order2", ex);
        System.out.println(ex);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.printf("%s - %s%n", new String(msg.getBody()), msg);
//                    System.out.println(new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

        });


        consumer.start();

        synchronized (ConsumerOrderTest.class) {
            ConsumerOrderTest.class.wait();
        }
//        Message msg = new Message("follow", "no_handler"
//                , "tag", "hello oa follow".getBytes(RemotingHelper.DEFAULT_CHARSET));
//        SendResult sendResult = producer.send(msg);
//        System.out.printf("%s%n", sendResult);
//        producer.shutdown();

    }
}
