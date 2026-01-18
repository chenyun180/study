package com.cloud.admin.model.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新增/编辑部门请求参数
 *
 * @author cloud
 */
@Data
public class DepartmentSaveRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 部门ID（编辑时必填）
     */
    private Long id;

    /**
     * 上级部门ID
     */
    private Long parentId;

    /**
     * 所属公司ID
     */
    @NotNull(message = "所属公司ID不能为空")
    private Long companyId;

    /**
     * 编码
     */
    @NotBlank(message = "部门编码不能为空")
    private String code;

    /**
     * 名称
     */
    @NotBlank(message = "部门名称不能为空")
    private String name;

    /**
     * 负责人ID
     */
    private Long leaderId;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态，默认active
     */
    private String status = "active";

    /**
     * 排序号，默认1
     */
    private Integer sort = 1;

    /**
     * 备注
     */
    private String remark;
}

