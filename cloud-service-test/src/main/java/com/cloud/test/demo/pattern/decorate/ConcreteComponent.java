package com.cloud.test.demo.pattern.decorate;

import org.springframework.stereotype.Service;

@Service
public class ConcreteComponent implements Component{

    @Override
    public void execute() {
        System.out.println("执行具体业务....");
    }

}
