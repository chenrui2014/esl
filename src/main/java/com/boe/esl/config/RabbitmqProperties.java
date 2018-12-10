package com.boe.esl.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="spring.rabbitmq")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RabbitmqProperties {

    private String host;
    private int port;
    private String username;
    private String password;
}
