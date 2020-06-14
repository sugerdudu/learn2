package com.gz.conf;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MegSender  implements InitializingBean {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    MsgConfirm msgConfirm;

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(" -- afterPropertiesSet");
        rabbitTemplate.setConfirmCallback(msgConfirm);
    }
}
