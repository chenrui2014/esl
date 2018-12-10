package com.boe.esl.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class RabbitmqConfiguration {
    @Resource
    private RabbitmqProperties rabbitmqProperties;

    public static final String EXCHANGE_NAME="amq.direct";
    public static final String DEVICE_QUEUE="device";
    public static final String UPDATE_QUEUE="update";
    public static final String WAREHOUSE_QUEUE="warehouse";
    public static final String DEVICE_ROUTING_KEY="state";
    public static final String UPDATE_ROUTING_KEY="label";
    public static final String WAREHOUSE_ROUTING_KEY="button";
    public static final String CONTROL_QUEUE="control";
    public static final String CONTROL_ROUTING_KEY="network";


    @Bean(name = "connectionFactory")
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(rabbitmqProperties.getHost());
        connectionFactory.setPort(rabbitmqProperties.getPort());
        connectionFactory.setUsername(rabbitmqProperties.getUsername());
        connectionFactory.setPassword(rabbitmqProperties.getPassword());

        return connectionFactory;
    }

    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(RabbitmqConfiguration.EXCHANGE_NAME);
    }

    @Bean
    public Queue controlDirectQueue() {
        return new Queue(RabbitmqConfiguration.CONTROL_QUEUE);
    }
    @Bean
    public Queue deviceDirectQueue() {
        return new Queue(RabbitmqConfiguration.DEVICE_QUEUE);
    }
    @Bean
    public Queue updateDirectQueue() {
        return new Queue(RabbitmqConfiguration.UPDATE_QUEUE);
    }
    @Bean
    public Queue warehouseDirectQueue() {
        return new Queue(RabbitmqConfiguration.WAREHOUSE_QUEUE);
    }
    @Bean
    Binding bindingDirectExchangeControl(Queue controlDirectQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(controlDirectQueue).to(directExchange).with(RabbitmqConfiguration.CONTROL_ROUTING_KEY);
    }
    @Bean
    Binding bindingDirectExchangeDevice(Queue deviceDirectQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(deviceDirectQueue).to(directExchange).with(RabbitmqConfiguration.DEVICE_ROUTING_KEY);
    }
    @Bean
    Binding bindingDirectExchangeUpdate(Queue updateDirectQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(updateDirectQueue).to(directExchange).with(RabbitmqConfiguration.UPDATE_ROUTING_KEY);
    }
    @Bean
    Binding bindingDirectExchangeWareHouse(Queue warehouseDirectQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(warehouseDirectQueue).to(directExchange).with(RabbitmqConfiguration.WAREHOUSE_ROUTING_KEY);
    }
}
