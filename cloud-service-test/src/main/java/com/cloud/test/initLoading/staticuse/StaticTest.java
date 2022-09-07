package com.cloud.test.initLoading.staticuse;

import org.springframework.stereotype.Component;

/**
 *  @author: chenyun
 *  @Date: 2020/6/26 11:07
 *  @Description: static代码块启动会执行；static方法不会
 */
@Component
public class StaticTest {

    static {
        System.out.println("static 测试=========================");
    }

    public static void loading() {
        System.out.println("测试loading......................");
    }

}
