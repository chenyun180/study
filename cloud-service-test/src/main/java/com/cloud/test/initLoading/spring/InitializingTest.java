package com.cloud.test.initLoading.spring;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

@Service
public class InitializingTest implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("initalizingBean..............................");
    }

}
