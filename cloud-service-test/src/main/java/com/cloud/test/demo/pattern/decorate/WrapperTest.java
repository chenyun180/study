package com.cloud.test.demo.pattern.decorate;

public class WrapperTest {

    public static void main(String[] args) {
        Wrapper2 wrapper2 = new Wrapper2(new Wrapper1(new ConcreteComponent()));
        wrapper2.execute();
    }

}
