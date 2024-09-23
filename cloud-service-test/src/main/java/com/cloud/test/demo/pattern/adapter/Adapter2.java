package com.cloud.test.demo.pattern.adapter;

/**
 * 组合实现：需要复写每一个方法
 */
public class Adapter2 implements ITarget{

    private Adaptee adaptee;

    public Adapter2(Adaptee adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public Integer f1() {
        String fa = adaptee.fa();
        return Integer.parseInt(fa);
    }

    @Override
    public void f2() {
        adaptee.fb();
    }

    @Override
    public void fc() {
        adaptee.fc();
    }
}
