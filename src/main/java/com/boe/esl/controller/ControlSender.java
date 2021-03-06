package com.boe.esl.controller;

import com.boe.esl.config.RabbitmqConfiguration;

import com.boe.esl.model.Label;
import com.boe.esl.vo.LabelVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ControlSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(LabelVO labelVO){
        rabbitTemplate.convertAndSend(RabbitmqConfiguration.EXCHANGE_NAME,RabbitmqConfiguration.CONTROL_ROUTING_KEY,labelVO);
    }
}
