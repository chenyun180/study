package com.cloud.admin.model.dto.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 岗位详情响应
 *
 * @author cloud
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionDetailResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 岗位ID
     */
    private Long id;

    /**
     * 上级岗位ID
     */
    private Long parentId;

    /**
     * 所属部门ID
     */
    private Long departmentId;

    /**
     * 编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 岗位级别
     */
    private Integer level;

    /**
     * 状态
     */
    private String status;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}

