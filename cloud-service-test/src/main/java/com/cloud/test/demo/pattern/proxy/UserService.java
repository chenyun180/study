package com.cloud.test.demo.pattern.proxy;

public interface UserService {

    String register(String userName, String password);

    void login(String userName, String password);

}
