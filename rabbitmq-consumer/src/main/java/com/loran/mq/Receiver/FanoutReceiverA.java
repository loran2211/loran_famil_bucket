package com.loran.mq.Receiver;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * @Author: luol
 * @Date: 2020/10/20 10:07
 * @Description:
 */
@Component
@RabbitListener(queues = "fanout.A")
public class FanoutReceiverA {

    @RabbitHandler
    public void process(Map testMessage) {
        System.out.println("FanoutReceiverA消费者收到消息  : " +testMessage.toString());
    }

}