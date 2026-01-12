package com.cloud.admin.model.dto.req;

import lombok.Data;

import java.io.Serializable;

/**
 * 获取组织机构列表请求参数
 *
 * @author cloud
 */
@Data
public class OrganizationListRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 父节点ID
     */
    private Long parentId;

    /**
     * 类型筛选：company/department/position/user
     */
    private String type;

    /**
     * 编码模糊搜索
     */
    private String code;

    /**
     * 名称模糊搜索
     */
    private String name;

    /**
     * 当前页码，默认1
     */
    private Integer current = 1;

    /**
     * 每页数量，默认10
     */
    private Integer size = 10;
}

