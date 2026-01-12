package com.cloud.admin.model.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 删除组织机构节点请求参数
 *
 * @author cloud
 */
@Data
public class OrganizationDeleteRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    @NotNull(message = "节点ID不能为空")
    private Long id;

    /**
     * 节点类型：company/department/position/user
     */
    @NotBlank(message = "节点类型不能为空")
    private String type;
}

