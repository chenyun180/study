package com.cloud.test.demo.pattern.observer;

public class ConcreteObserverOne implements  Observer {

    @Override
    public void updateMessage(Message message) {
        System.out.println("observer one receive a message...");
    }

}
