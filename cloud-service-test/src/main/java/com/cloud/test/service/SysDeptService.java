package com.cloud.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.test.entity.SysDept;

import java.util.List;

public interface SysDeptService extends IService<SysDept> {

    List<SysDept> getAll();

}
