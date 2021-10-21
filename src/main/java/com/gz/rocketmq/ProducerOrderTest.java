package com.gz.rocketmq;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

/**
 * 有序生产，将数据打到同一队列
 */
public class ProducerOrderTest {
    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("oa-group2");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.setDefaultTopicQueueNums(8);
//        producer.setSendMsgTimeout(10000);
        producer.start();

        for (int i = 0; i < 100; i++) {
            String body = "hello oa follow " +i;
            Message msg = new Message("follow-order2", "no_handler1"
                    , "tag", ("hello oa follow " +i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    int i = new Random().nextInt(8);
//                    int i = arg.toString().hashCode() % 7;

                    System.out.println(i);
                    return mqs.get(i);
                }
            },"aaa" + i);
            System.out.printf("%s - %s%n", body,sendResult);
            //Thread.sleep(100);
        }


        producer.shutdown();


        synchronized (ProducerTest.class) {
            ProducerTest.class.wait();
        }

    }
}
