package com.cloud.test.initLoading.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 *  @author: chenyun
 *  @Date: 2020/6/26 10:18
 *  @Description: BeanPostProcessor会打印每个bean的初始化前后的信息（每个bean都会打印一次）。不适合一次性加载的场景。
 */
//@Service
public class PostProcessorTest implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization..................");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization==============================");
        return bean;
    }

}
