package com.cloud.test.demo.pattern.adapter;

public class AdapterTest {

    public static void main(String[] args) {
        adapter2Test();
    }


    public static void adapter2Test() {
        ITarget target = new Adapter2(new Adaptee());
        System.out.println(target.f1());
//        target.f2();
//        target.fc();
    }

    public static void adapterTest() {
        ITarget target = new Adapter();
//        System.out.println(target.f1());
        target.f2();
//        target.fc();
    }

}
