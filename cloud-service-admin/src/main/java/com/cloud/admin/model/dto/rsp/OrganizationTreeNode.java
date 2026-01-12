package com.cloud.admin.model.dto.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 组织机构树节点响应
 *
 * @author cloud
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationTreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    private Long id;

    /**
     * 父节点ID，根节点为null
     */
    private Long parentId;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型：company/department/position/user
     */
    private String type;

    /**
     * 完整路径
     */
    private String path;

    /**
     * 状态：active/inactive
     */
    private String status;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 子节点列表
     */
    private List<OrganizationTreeNode> children;
}

