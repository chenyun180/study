package com.cloud.test.initLoading.commandline;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class CommandLineTestOne implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("commandLineOne=====================");
    }

}
