package com.cloud.admin.model.dto.rsp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 保存返回ID响应
 *
 * @author cloud
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveIdResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;
}

