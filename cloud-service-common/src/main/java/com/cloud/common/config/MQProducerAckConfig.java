package com.cloud.common.config;

import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 *  mq 确认回调配置。需手动开启确认机制
 */
@Component
public class MQProducerAckConfig implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    private static final Logger log = LoggerFactory.getLogger(MQProducerAckConfig.class);

    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * 确认消息是否到达交换机。若已达到，ack=true；否则，ack = false
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(ack){
            log.info("消息发送成功:{}", correlationData);
        } else {
            log.info("消息发送失败:{}", correlationData);
        }

    }

    /**
     * 确认消息是否达到队列。
     * 若exChange到queue失败，则回调此方法；否则，不回调此方法
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exChange, String routingKey) {
        log.info("应答信息为:{},{}", replyCode, replyText);
        log.info("消息主体为:" + SerializationUtils.deserialize(message.getBody()));
        log.info("路由信息为{},{}", exChange, routingKey);

    }
}
