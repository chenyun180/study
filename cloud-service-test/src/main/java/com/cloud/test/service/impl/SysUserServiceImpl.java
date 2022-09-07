package com.cloud.test.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.test.entity.SysUser;
import com.cloud.test.mapper.SysUserMapper;
import com.cloud.test.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysUser> getAll(){
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        List<SysUser> list = this.list();
        return list;
    }
}
