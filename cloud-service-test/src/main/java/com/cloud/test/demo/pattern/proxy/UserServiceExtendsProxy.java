package com.cloud.test.demo.pattern.proxy;

import org.springframework.stereotype.Service;

@Service
public class UserServiceExtendsProxy extends UserServiceImpl {

    @Override
    public String register(String userName, String password) {
        long start = System.currentTimeMillis();

        //根据userName和password注册
        super.register(userName, password);

        long end = System.currentTimeMillis();
        return "UserServiceExtendsProxy register...";
    }

    @Override
    public void login(String userName, String password) {
        long start = System.currentTimeMillis();

        //根据userName和password登录
        super.login(userName, password);

        long end = System.currentTimeMillis();
    }

}
