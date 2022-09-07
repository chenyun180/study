package com.cloud.test.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.test.entity.SysRole;
import com.cloud.test.entity.SysUser;

import java.util.List;

public interface SysUserService extends IService<SysUser> {

    List<SysUser> getAll();


}
