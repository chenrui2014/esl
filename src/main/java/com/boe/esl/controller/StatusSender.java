package com.boe.esl.controller;

import com.boe.esl.config.RabbitmqConfiguration;
import com.boe.esl.model.Label;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StatusSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(Label label){
        rabbitTemplate.convertAndSend(RabbitmqConfiguration.EXCHANGE_NAME,RabbitmqConfiguration.DEVICE_ROUTING_KEY,label);
    }
}
