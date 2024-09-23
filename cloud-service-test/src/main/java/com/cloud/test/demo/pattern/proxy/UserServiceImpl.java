package com.cloud.test.demo.pattern.proxy;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Override
    public String register(String userName, String password) {
        long start = System.currentTimeMillis();

        //根据userName和password注册

        long end = System.currentTimeMillis();

        return "userServiceImpl register....";
    }

    @Override
    public void login(String userName, String password) {
        long start = System.currentTimeMillis();

        //根据userName和password登录

        long end = System.currentTimeMillis();
    }
}
