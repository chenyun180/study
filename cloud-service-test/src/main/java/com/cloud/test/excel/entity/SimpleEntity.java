package com.cloud.test.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SimpleEntity {

//    @ExcelProperty(value = "姓名")
    private String name;


    @ExcelProperty(value = "身高")
    @NumberFormat("#.#")
    //这个类型必须是String，不能是BigDecimal等
    private String height;


    @ExcelProperty(value = "生日")
    @DateTimeFormat("yyyy年MM月dd日 HH:mm:ss")
    private String birth;

}
