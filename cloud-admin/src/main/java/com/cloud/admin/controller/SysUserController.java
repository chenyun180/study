package com.cloud.admin.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.cloud.admin.client.TestClient;

@RestController
@RequestMapping(value = "/admin/sysUser", produces = "application/json;charset=utf-8")
public class SysUserController {

    @Resource
    private TestClient testClient;

    /**
     * 若调用远程服务异常(如：testClient.getOne接口未启动成功)，那么会调到fallback类中服务中
     */
    @PostMapping(value = "/testFeign")
    public void testFeign() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "5");
        String result = testClient.getOne(jsonObject);
    }


}
