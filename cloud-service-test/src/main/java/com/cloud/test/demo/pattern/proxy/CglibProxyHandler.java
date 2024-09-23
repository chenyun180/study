package com.cloud.test.demo.pattern.proxy;

import com.alibaba.fastjson.JSONObject;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CglibProxyHandler implements MethodInterceptor{

    private Object target;

    public CglibProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        System.out.println("before cglib intercept: " + JSONObject.toJSONString(args));

        Object result = methodProxy.invokeSuper(o, args);
        System.out.println("after cglib result: " + JSONObject.toJSONString(result));
        return result;
    }




}
