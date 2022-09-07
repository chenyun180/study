package com.cloud.common.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 关联分页查询,需要排序时调用这个BasePage;
 */
public class BasePage<T> extends Page<T> {

    public BasePage(long current, long size){super(current, size);}

    private String orderBy;//自定义排序

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
