package com.cloud.test.demo.pattern.adapter;

import org.springframework.stereotype.Service;

/**
 * ji继承
 * 其中fc方法可以不实现。因为可以从Adaptee继承
 */
@Service
public class Adapter extends Adaptee implements ITarget{


    @Override
    public Integer f1() {
        String fa = super.fa();
        return Integer.parseInt(fa);
    }

    @Override
    public void f2() {
        super.fb();
        System.out.println("Adapter impl f2..");
    }


}
