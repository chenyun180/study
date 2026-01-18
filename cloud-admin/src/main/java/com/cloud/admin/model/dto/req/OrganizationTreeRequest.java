package com.cloud.admin.model.dto.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 获取组织机构树请求参数
 *
 * @author cloud
 */
@Data
public class OrganizationTreeRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 搜索关键词，用于过滤节点名称
     */
    private String keyword;
}

