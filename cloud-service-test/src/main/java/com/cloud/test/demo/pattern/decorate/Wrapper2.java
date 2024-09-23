package com.cloud.test.demo.pattern.decorate;


public class Wrapper2 implements Component{

    private Component component;

    public Wrapper2(Component component) {
        this.component = component;
    }

    @Override
    public void execute() {
        System.out.println("wrapper2 begin.....");
        component.execute();
        System.out.println("wrapper2 end.....");
    }

}
