package com.cloud.test.demo.pattern.observer;

public class ObserverTest {

    public static void main(String[] args) {
        Subject subject = new ConcreteSubject();
        subject.registerObserver(new ConcreteObserverOne());
        subject.registerObserver(new ConcreteObserverTwo());
        subject.notifyObservers(new Message());
    }

}
