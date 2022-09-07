package com.cloud.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.test.entity.SysRole;
import com.cloud.test.mapper.SysRoleMapper;
import com.cloud.test.service.SysRoleService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private static final Logger logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);

    @Override
    public List<SysRole> getAll() {
        List<SysRole> list = Lists.newArrayList();
        try {
            list = this.list();
        } catch (Exception e){
            logger.error("sysRoleServiceImpl getAll exception", e);
        }
        return list;
    }
}
