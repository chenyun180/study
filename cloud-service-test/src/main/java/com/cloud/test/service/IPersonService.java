package com.cloud.test.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.common.model.test.PersonModel;
import com.cloud.common.utils.service.ResultData;
import com.cloud.test.entity.Person;

import java.util.List;

public interface IPersonService extends IService<Person> {

    List<PersonModel> personAll(Page<Person> page);

    ResultData<PersonModel> getPersonById(Long id);

    void testMq(String routingKey);

    ResultData<Boolean> testExecutor(JSONObject jsonObject);

    ResultData<Boolean> testOptimism(Long id);

}
