package com.cloud.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.test.entity.SysDept;
import com.cloud.test.mapper.SysDeptMapper;
import com.cloud.test.service.SysDeptService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {


    @Override
    public List<SysDept> getAll() {
        return this.list();
    }
}
