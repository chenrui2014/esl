package com.boe.esl.controller;

import com.boe.esl.config.RabbitmqConfiguration;
import com.boe.esl.model.Label;
import com.boe.esl.vo.UpdateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdateSender {
    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(UpdateVO updateVO){
        rabbitTemplate.convertAndSend(RabbitmqConfiguration.EXCHANGE_NAME,RabbitmqConfiguration.UPDATE_ROUTING_KEY,updateVO);
    }
}
