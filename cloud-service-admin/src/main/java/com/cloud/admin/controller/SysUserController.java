package com.cloud.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.cloud.admin.client.TestClient;
import com.cloud.common.model.test.PersonModel;
import com.cloud.common.utils.service.BaseController;
import com.cloud.common.utils.service.ServiceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/admin/sysUser", produces = "application/json;charset=utf-8")
public class SysUserController extends BaseController {

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
        PersonModel personModel = ServiceUtils.parseObject(result, PersonModel.class);
        System.out.println("返回信息为：" + personModel.getPersonAge() + personModel.getPersonName());
    }


}
