package com.cloud.test.controller;

import com.cloud.common.model.test.PersonCopy;
import com.cloud.common.utils.service.BaseController;
import com.cloud.test.service.IPersonCopyService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test/personCopy", produces = "application/json;charset=utf-8")
public class PersonCopyController extends BaseController {

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private IPersonCopyService personCopyService;

    @PostMapping("/savePerson")
    public String savePerson(@RequestBody PersonCopy personCopy){
        return callbackSuccess(personCopyService.savePerson(personCopy));
    }

    @PostMapping("/testCommonThreadPool")
    public void testCommonThreadPool(){
        personCopyService.testCommonThreadPool();
    }

}
