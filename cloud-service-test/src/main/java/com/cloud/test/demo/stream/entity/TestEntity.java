package com.cloud.test.demo.stream.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestEntity {

    private String startTime;
    private String endTime;
    private String flag;
}
