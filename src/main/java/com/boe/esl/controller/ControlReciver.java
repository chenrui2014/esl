//package com.boe.esl.controller;
//
//import com.boe.esl.model.Label;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Component
//@RabbitListener(queues = "control")
//@Slf4j
//public class ControlReciver {
//
//    @RabbitHandler
//    public void process(Label label) {
//        log.info("接收消息："+label.getCode());
//        log.info("接收消息时间："+new Date());
//        System.out.println("设备ID:" + label.getCode());
//    }
//}
