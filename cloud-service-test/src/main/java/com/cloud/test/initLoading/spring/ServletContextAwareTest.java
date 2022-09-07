package com.cloud.test.initLoading.spring;

import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 *  ServletContextAware：在填充bean属性之后，初始化之前调用.只调用一次
 */
@Service
public class ServletContextAwareTest implements ServletContextAware {

    @Override
    public void setServletContext(ServletContext servletContext) {
        System.out.println("setServletContext=============================");
    }

}
