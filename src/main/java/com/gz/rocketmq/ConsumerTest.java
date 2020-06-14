package com.gz.rocketmq;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;

import java.util.List;

public class ConsumerTest {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("oa-group");
        consumer.setNamesrvAddr("127.0.0.1:9876;127.0.0.1:9877");
//        consumer.setMessageModel(MessageModel.BROADCASTING);
        String ex = "*";
        consumer.subscribe("follow", ex);
        System.out.println(ex);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.print("1");
                for (MessageExt msg : msgs) {
                    System.out.println(msg);
//                    System.out.println(new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

        });


        consumer.start();


//        Message msg = new Message("follow", "no_handler"
//                , "tag", "hello oa follow".getBytes(RemotingHelper.DEFAULT_CHARSET));
//        SendResult sendResult = producer.send(msg);
//        System.out.printf("%s%n", sendResult);
//        producer.shutdown();

    }
}
