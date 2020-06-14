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

public class ProducerTest {
    public static void main(String[] args) throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer producer = new DefaultMQProducer("oa-group");
        producer.setNamesrvAddr("127.0.0.1:9877");
//        producer.setSendMsgTimeout(10000);
        producer.start();

        for (int i = 0; i < 10000; i++) {
            Message msg = new Message("follow", "no_handler"
                    , "tag", ("hello oa follow " +i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    //System.out.println(arg);

                    return mqs.get(3);
                }
            },"aaa");
            System.out.printf("%s%n", sendResult);
            Thread.sleep(1000);
        }


        producer.shutdown();

    }
}
