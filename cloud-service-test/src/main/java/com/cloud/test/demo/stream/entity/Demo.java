package com.cloud.test.demo.stream.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class Demo {

    private Long id;

    private Integer type;

    private String name;

    private String time;

    private Date date;

    private LocalDateTime localTime;

}
