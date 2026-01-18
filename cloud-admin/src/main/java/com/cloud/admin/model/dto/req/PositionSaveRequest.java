package com.cloud.admin.model.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新增/编辑岗位请求参数
 *
 * @author cloud
 */
@Data
public class PositionSaveRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位ID（编辑时必填）
     */
    private Long id;

    /**
     * 上级岗位ID
     */
    private Long parentId;

    /**
     * 所属部门ID
     */
    @NotNull(message = "所属部门ID不能为空")
    private Long departmentId;

    /**
     * 编码
     */
    @NotBlank(message = "岗位编码不能为空")
    private String code;

    /**
     * 名称
     */
    @NotBlank(message = "岗位名称不能为空")
    private String name;

    /**
     * 岗位级别（1-10）
     */
    private Integer level;

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

