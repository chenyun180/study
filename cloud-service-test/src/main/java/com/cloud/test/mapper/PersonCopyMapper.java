package com.cloud.test.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.common.model.test.PersonCopy;
import com.cloud.test.entity.Person;
import org.apache.ibatis.annotations.Param;

/**
 * 测试Mapper
 */
public interface PersonCopyMapper extends BaseMapper<PersonCopy> {


}
