package com.loran.mq.Receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author: luol
 * @Date: 2020/10/22 8:50
 * @Description:
 */
@Component
@RabbitListener(queues = "DeadLetterQueue")
public class DeadLetterReceiver {
    @RabbitHandler
    public void process(Map testMessage) {
        System.out.println("DeadLetterQueue消费者收到消息  : " +testMessage.toString());
    }
}
