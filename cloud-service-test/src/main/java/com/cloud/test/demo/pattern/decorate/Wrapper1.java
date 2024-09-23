package com.cloud.test.demo.pattern.decorate;

public class Wrapper1 implements Component{

    private Component component;

    public Wrapper1(Component component) {
        this.component = component;
    }


    @Override
    public void execute() {
        System.out.println("wrapper1 begin.....");
        component.execute();
        System.out.println("wrapper1 end.....");
    }

}
