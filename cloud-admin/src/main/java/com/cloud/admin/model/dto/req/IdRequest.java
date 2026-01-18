package com.cloud.admin.model.dto.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 通用ID请求参数
 *
 * @author cloud
 */
@Data
public class IdRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @NotNull(message = "ID不能为空")
    private Long id;
}

