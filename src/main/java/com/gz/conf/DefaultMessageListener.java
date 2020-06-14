package com.gz.conf;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.util.Map;

public class DefaultMessageListener   { //implements MessageListener

    /*@Override
    public void onMessage(Message message) {
        System.out.println(message.toString());
    }*/

    public void handleMessage(String msg) {
        System.out.println("---------- handleMessage --------------");
        System.out.println(msg);

    }

    public void msgTask(Map<String,Object> map) {
        System.out.println("---------- msgTask --------------");
        System.out.println(map.toString());

    }

    public void msgFollow(Map<String,Object> map) {
        System.out.println("---------- msgFollow --------------");
        System.out.println(map.toString());

    }

    public void msgDelay(Map<String,Object> map) {
        System.out.println("---------- msgDelay --------------");
        System.out.println(map.toString());

    }
}
