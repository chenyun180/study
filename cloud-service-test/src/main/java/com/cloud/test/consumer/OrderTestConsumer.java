package com.cloud.test.consumer;

import com.alibaba.fastjson.JSONObject;
import com.cloud.common.constants.RabbitmqConstants;
import com.rabbitmq.client.Channel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OrderTestConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderTestConsumer.class);

    @RabbitListener(queues = RabbitmqConstants.QUEUE_CLOUD_TEST_FIRST)
    public void testFirst(String msg, Message message, Channel channel) throws IOException {
        try{
            JSONObject jsonObject = JSONObject.parseObject(msg);
            String testStr = jsonObject.getString("testStr");
            testStr.equals("1111");

            log.info("error message...........");
            //确认收到消息 true表示收到所有consumer获得的消息，false表示当前收到的一个消息
//            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {

            //是否已经重复处理过
            if(message.getMessageProperties().getRedelivered()){
                log.info("已重复处理失败，不在处理");
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                log.info("消息即将返回队列处理....");
                //false:当前一条
                //true：重新处理
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            }
        }

    }

    @RabbitListener(queues = RabbitmqConstants.QUEUE_CLOUD_TEST_SECOND)
    public void testSecond(String msg, Message message,Channel channel ) throws IOException{
        JSONObject jsonObject = JSONObject.parseObject(msg);
        String testStr = jsonObject.getString("testStr");
        System.out.println("testSecond:" + testStr);
//        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
    }

    //死信消息
//    @RabbitListener(queues = RabbitmqConstant.QUEUE_DEAD)
//    public void testDead(String msg, Message  message, Channel channel) throws IOException{
//        JSONObject jsonObject = JSONObject.parseObject(msg);
//        String testStr = jsonObject.getString("testStr");
//        System.out.println("testDead:" + testStr);
//        System.out.println(testStr.equals("ww"));
//        //确认消息
//        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
//    }

}
