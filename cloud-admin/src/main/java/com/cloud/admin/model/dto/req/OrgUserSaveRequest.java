package com.cloud.admin.model.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新增/编辑组织用户请求参数
 *
 * @author cloud
 */
@Data
public class OrgUserSaveRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID（编辑时必填）
     */
    private Long id;

    /**
     * 所属岗位ID
     */
    @NotNull(message = "所属岗位ID不能为空")
    private Long positionId;

    /**
     * 编码
     */
    @NotBlank(message = "用户编码不能为空")
    private String code;

    /**
     * 名称
     */
    @NotBlank(message = "用户姓名不能为空")
    private String name;

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

