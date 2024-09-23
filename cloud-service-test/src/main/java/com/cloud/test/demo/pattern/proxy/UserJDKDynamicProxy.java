package com.cloud.test.demo.pattern.proxy;

import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UserJDKDynamicProxy implements InvocationHandler {

    private Object target;

    public UserJDKDynamicProxy(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("动态代理前置操作, args=" + JSONObject.toJSONString(args));

        Object invoke = method.invoke(target, args);

        System.out.println("动态代理后置操作, result=" + JSONObject.toJSONString(invoke));
        return invoke;

    }


}
