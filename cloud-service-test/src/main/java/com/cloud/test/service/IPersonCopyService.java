package com.cloud.test.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.common.model.test.PersonCopy;
import com.cloud.common.model.test.PersonModel;
import com.cloud.common.utils.service.ResultData;
import com.cloud.test.entity.Person;

import java.util.List;

public interface IPersonCopyService extends IService<PersonCopy> {

    ResultData<Boolean> savePerson(PersonCopy personCopy);

    void testCommonThreadPool();

}
