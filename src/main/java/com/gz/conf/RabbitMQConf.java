package com.gz.conf;

//import com.alibaba.druid.pool.DruidDataSource;

import org.redisson.Redisson;
import org.redisson.config.Config;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConf {

//    @Bean
//    public ConnectionFactory connectionFactory(){
//        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
//        connectionFactory.setHost("127.0.0.1");
//        connectionFactory.setPort(5672);
//        connectionFactory.setVirtualHost("/oa");
//        connectionFactory.setUsername("cgq");
//        connectionFactory.setPassword("111111");
//        connectionFactory.setConnectionTimeout(10000);
//        return connectionFactory;
//    }



    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        //自动识别队列、交换机等信息
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public CustomExchange customExchange(){
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        CustomExchange exchange = new CustomExchange("oa3.delay.exchange", "x-delayed-message", true, false, args);
        return exchange;
    }

    /**
     * 主题交换机
     */
    @Bean
    public TopicExchange topicExchange(){
        TopicExchange topicExchange = new TopicExchange("oa3.topic.exchange", true, false, null);
        return topicExchange;
    }

    /**
     * 扇形交换机
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        FanoutExchange fanoutExchange = new FanoutExchange("oa3.fanout.exchange", true, false, null);
        return fanoutExchange;
    }

    /**
     * 直接交换机
     */
    @Bean
    public DirectExchange directExchange(){
        DirectExchange directExchange = new DirectExchange("oa3.direct.exchange", true, false, null);
        return directExchange;
    }

    @Bean("oaTaskTopicQueue")
    public Queue oaTopicQueue(){
        Queue queue = new Queue("oa3.task", true, false, false, null);
        return queue;
    }

    @Bean("oaFollowTopicQueue")
    public Queue followTopicQueue(){
        Queue queue = new Queue("oa3.follow", true, false, false, null);
        return queue;
    }

    @Bean("oaTaskDelayQueue")
    public Queue taskDelayQueue(){
        Queue queue = new Queue("oa3.delay.task", true, false, false, null);
        return queue;
    }

    @Bean("topicBindingOATask")
    public Binding topicBindingOATask(@Qualifier("oaTaskTopicQueue") Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with("oa3.task.#");
    }

    @Bean("topicBindingOAFollow")
    public Binding topicBindingOAFollow(@Qualifier("oaFollowTopicQueue") Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue).to(topicExchange).with("oa3.follow.#");
    }

    @Bean("delayBindingOATask")
    public Binding delayBindingOATask(@Qualifier("oaTaskDelayQueue") Queue queue, CustomExchange customExchange) {
        return BindingBuilder.bind(queue).to(customExchange).with("oa3.delay.task.add").noargs();
    }

    @Bean
    public SimpleMessageListenerContainer simpleMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);

        container.setQueueNames("oa3.follow","oa3.delay.task");//"oa3.task"
        //消费者线程数量
        container.setConcurrentConsumers(5);
        container.setMaxConcurrentConsumers(10);
        //配置签收模式：自动签收
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        //设置拒绝重回队列
        container.setDefaultRequeueRejected(false);

        //配置默认监听器
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(new DefaultMessageListener());
//        messageListenerAdapter.setDefaultListenerMethod("msg");
        messageListenerAdapter.setMessageConverter(jsonMessageConverter());

        Map<String, String> map = new HashMap<>();
//        map.put("oa3.task", "msgTask");
        map.put("oa3.follow", "msgFollow");
        map.put("oa3.delay.task", "msgDelay");
        messageListenerAdapter.setQueueOrTagToMethodName(map);

        container.setMessageListener(messageListenerAdapter);

        return container;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setReceiveTimeout(5000);
        return rabbitTemplate;
    }

}
