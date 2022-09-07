package com.cloud.admin.client;

import com.alibaba.fastjson.JSONObject;
import com.cloud.admin.hystrix.TestClientImpl;
import com.cloud.common.constants.CloudServiceList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * value：远程调用的微服务的服务名称；fallback：接口熔断后的回调类
 *
 */
@FeignClient(value = CloudServiceList.CLOUD_SERVICE_TEST, fallback = TestClientImpl.class)
public interface TestClient {

    @PostMapping("/person/getOne")
    String getOne(@RequestBody JSONObject jsonObject);

}
