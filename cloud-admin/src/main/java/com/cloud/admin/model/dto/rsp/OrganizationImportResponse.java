package com.cloud.admin.model.dto.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 组织机构导入响应
 *
 * @author cloud
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationImportResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功条数
     */
    private Integer success;

    /**
     * 失败条数
     */
    private Integer fail;

    /**
     * 错误详情
     */
    private List<String> errors;
}

