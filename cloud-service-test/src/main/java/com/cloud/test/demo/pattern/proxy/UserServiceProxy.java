package com.cloud.test.demo.pattern.proxy;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceProxy implements UserService{

    @Resource(name = "userServiceImpl")
    private UserService userService;

    @Override
    public String register(String userName, String password) {
        long start = System.currentTimeMillis();
        // 注册的逻辑
        userService.register(userName, password);
        long end = System.currentTimeMillis();

        return String.valueOf(end - start);
    }

    @Override
    public void login(String userName, String password) {
        long start = System.currentTimeMillis();
        // 登录的逻辑
        userService.register(userName, password);
        long end = System.currentTimeMillis();
    }
}
