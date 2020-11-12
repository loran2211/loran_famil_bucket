package com.loran.mq.Receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: luol
 * @Date: 2020/10/20 9:50
 * @Description:
 */
@Component
@RabbitListener(queues = "topic.man")
public class TopicManReceiver {
    @RabbitHandler
    public void process(Map testMessage) {
        System.out.println("TopicManReceiver消费者收到消息  : " + testMessage.toString());
    }
}
