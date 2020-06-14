import com.gz.AppTest2;
import com.rabbitmq.client.AMQP;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppTest2.class)//,args="--spring.profiles.active=local"
public class RabbitMQTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void send(){
        Map<String, Object> headers = new HashMap<>();
        headers.put("name", "suger");
        headers.put("company", "hz gz");

        MessageProperties prop = new MessageProperties();
        prop.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        prop.setExpiration("10000");
        prop.setContentEncoding("utf-8");
        prop.setCorrelationId(UUID.randomUUID().toString());
        prop.setHeader("name", "suger");
        prop.setHeader("company", "hz gz");

        for (int i = 0; i <10; i++) {
            Message message = new Message( ("{\"a\":\"aaaa "+i+"\"}").getBytes(), prop);
            rabbitTemplate.convertAndSend("oa3.topic.exchange","oa3.task.add", message);
        }


        Message message2 = new Message( "{\"bbbb\":\"bbbbbb\"}".getBytes(), prop);
        rabbitTemplate.convertAndSend("oa3.topic.exchange","oa3.follow.add", message2);
    }

    @Test
    public void sendDelay(){
        Map<String, Object> headers = new HashMap<>();
        headers.put("name", "suger");
        headers.put("company", "hz gz");

        MessageProperties prop = new MessageProperties();
        prop.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        prop.setExpiration("10000");
        prop.setContentEncoding("utf-8");
        prop.setCorrelationId(UUID.randomUUID().toString());
        prop.setHeader("name", "suger delay");
        prop.setHeader("company", "hz gz");
        //方式一
        prop.setHeader("x-delay", "10000");
        Message message = new Message( "{\"a\":\"delay 10\"}".getBytes(), prop);

        rabbitTemplate.convertAndSend("oa3.delay.exchange", "oa3.delay.task.add", message);

        //方式二
//        rabbitTemplate.convertAndSend("oa3.delay.exchange", "oa3.delay.task.add", message, new MessagePostProcessor() {
//            @Override
//            public Message postProcessMessage(Message message) throws AmqpException {
//                System.out.println("x-delay");
//                message.getMessageProperties().setHeader("x-delay",5000);
//                return message;
//            }
//        });

    }
}
