package com.cloud.flow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@EnableFeignClients
@EnableDiscoveryClient
@EntityScan("com.cloud.common.model.flow")
@ComponentScan("com.cloud.common")
@ComponentScan("com.cloud.flow")
@SpringBootApplication
public class ServiceFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceFlowApplication.class, args);
    }
}
