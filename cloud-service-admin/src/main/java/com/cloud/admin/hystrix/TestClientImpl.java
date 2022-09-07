package com.cloud.admin.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.admin.client.TestClient;
import com.cloud.common.model.admin.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

/**
 * 熔断回调类，必须实现远程调用的client接口。
 */
@Service
public class TestClientImpl implements TestClient {

    private static final Logger logger = LoggerFactory.getLogger(TestClientImpl.class);

    @Override
    public String getOne(JSONObject jsonObject) {
        logger.error("进入熔断服务，输入的信息={}", JSONObject.toJSONString(jsonObject));

        JSONObject error = new JSONObject();
        error.put("id", "5");
        error.put("personName", "降级Name");
        return JSONObject.toJSONString(error);
    }

}
