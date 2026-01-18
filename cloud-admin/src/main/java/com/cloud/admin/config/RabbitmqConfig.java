package com.cloud.admin.config;

import com.cloud.common.constants.RabbitmqConstants;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    //声明测试相关交换机
    @Bean(RabbitmqConstants.EXCHANGE_CLOUD_TEST)
    public Exchange EXCHANGE_CLOUD_TEST() {
        //durable(true)持久化，mq重启后交换机还在
        return ExchangeBuilder.topicExchange(RabbitmqConstants.EXCHANGE_CLOUD_TEST).durable(true).build();
    }

    //声明测试1的队列
    @Bean(RabbitmqConstants.QUEUE_CLOUD_TEST_FIRST)
    public Queue QUEUE_CLOUD_TEST_FIRST() {
        return new Queue(RabbitmqConstants.QUEUE_CLOUD_TEST_FIRST);
    }

    //绑定测试1的队列到交换机
    @Bean
    public Binding BINDING_ROUTING_CLOUD_TEST_FIRST(@Qualifier(RabbitmqConstants.QUEUE_CLOUD_TEST_FIRST) Queue queue,
                                                   @Qualifier(RabbitmqConstants.EXCHANGE_CLOUD_TEST) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitmqConstants.ROUTINGKEY_CLOUD_TEST_FIRST).noargs();
    }

    //声明测试2的队列
    @Bean(RabbitmqConstants.QUEUE_CLOUD_TEST_SECOND)
    public Queue QUEUE_CLOUD_TEST_SECOND() {
        return new Queue(RabbitmqConstants.QUEUE_CLOUD_TEST_SECOND);
    }

    //绑定测试2的队列到交换机
    @Bean
    public Binding BINDING_QUEUE_CLOUD_TEST_SECOND(@Qualifier(RabbitmqConstants.QUEUE_CLOUD_TEST_SECOND) Queue queue,
                                                   @Qualifier(RabbitmqConstants.EXCHANGE_CLOUD_TEST) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(RabbitmqConstants.ROUTINGKEY_CLOUD_TEST_SECOND).noargs();
    }

}
