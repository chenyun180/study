package com.cloud.common.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 通用配置
 */
@Configuration
public class CommonConfig {
    /**
     * openfegin内置了ribbon，使用ribbon进行负载均衡；ribbon通过RestTemplate调用远程服务
     * （服务间的调用通过RestTemplate）
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }



}
