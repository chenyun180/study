package com.cloud.search.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/search", produces = "application/json;charset=utf-8")
public class GoodsSearchController {


    @PostMapping("/test")
    public String test() {
        System.out.println("11111111----------------");
        return "1111";
    }

}
