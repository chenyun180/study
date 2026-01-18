package com.cloud.gateway;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableDiscoveryClient
@ComponentScan("com.cloud.common")
@ComponentScan("com.cloud.gateway")
public class GatewayClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayClientApplication.class, args);
    }
}
