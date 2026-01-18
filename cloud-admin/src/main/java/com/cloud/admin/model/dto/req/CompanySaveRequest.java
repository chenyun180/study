package com.cloud.admin.model.dto.req;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 新增/编辑公司请求参数
 *
 * @author cloud
 */
@Data
public class CompanySaveRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公司ID（编辑时必填）
     */
    private Long id;

    /**
     * 上级公司ID
     */
    private Long parentId;

    /**
     * 编码
     */
    @NotBlank(message = "公司编码不能为空")
    private String code;

    /**
     * 名称
     */
    @NotBlank(message = "公司名称不能为空")
    private String name;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 地址
     */
    private String address;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 法人
     */
    private String legalPerson;

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

