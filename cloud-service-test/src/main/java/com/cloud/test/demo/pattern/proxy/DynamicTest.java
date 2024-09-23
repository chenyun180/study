package com.cloud.test.demo.pattern.proxy;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Proxy;

public class DynamicTest {

    public static void main(String[] args) {
//        jdkProxy();
        cglibProxy();
    }

    public static void cglibProxy() {
        CglibProxyHandler cglibProxyHandler = new CglibProxyHandler(new UserServiceImpl());
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserServiceImpl.class);
        enhancer.setCallback(cglibProxyHandler);
        UserServiceImpl userService = (UserServiceImpl) enhancer.create();
        String res = userService.register("cloud", "111");
        System.out.println("cglib result:" + res);
    }

    public static void jdkProxy() {
        UserService userService = new UserServiceImpl();
        UserJDKDynamicProxy jdkProxy = new UserJDKDynamicProxy(userService);
        UserService userServiceProxy = (UserService) Proxy.newProxyInstance(userService.getClass().getClassLoader(),
                userService.getClass().getInterfaces(),
                jdkProxy);

        userServiceProxy.register("cloud", "111");
    }

}
