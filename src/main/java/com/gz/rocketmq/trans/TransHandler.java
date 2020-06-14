package com.gz.rocketmq.trans;

import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.messaging.Message;

import java.util.Random;

@RocketMQTransactionListener
public class TransHandler implements RocketMQLocalTransactionListener {
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        System.out.println("-----------------------");
        System.out.println(message);
        System.out.println(o);
        System.out.println("executeLocalTransaction");
        System.out.println("-----------------------");

        return RocketMQLocalTransactionState.UNKNOWN;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        System.out.println("===================================");
        System.out.println("checkLocalTransaction");
        System.out.println(message);
        System.out.println("===================================");

        if(new Random().nextInt(8) / 2 ==0){
            return RocketMQLocalTransactionState.COMMIT;
        }

        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
