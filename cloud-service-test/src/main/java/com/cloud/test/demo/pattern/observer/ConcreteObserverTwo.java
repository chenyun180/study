package com.cloud.test.demo.pattern.observer;

public class ConcreteObserverTwo implements Observer {

    @Override
    public void updateMessage(Message message) {
        System.out.println("observer two receive a message...");
    }

}
