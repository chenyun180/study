package com.cloud.search.controller;

import com.cloud.common.model.test.PersonCopy;
import com.cloud.common.utils.service.ServiceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.cloud.common.utils.service.ServiceUtils.callbackSuccess;

@RestController
@RequestMapping(value = "/search", produces = "application/json;charset=utf-8")
public class GoodsSearchController {


    @PostMapping("/test")
    public String test() {
        System.out.println("11111111----------------");
        return "1111";
    }

}
