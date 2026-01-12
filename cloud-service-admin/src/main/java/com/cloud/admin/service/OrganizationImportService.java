package com.cloud.admin.service;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.cloud.admin.model.dto.rsp.OrganizationImportResponse;
import com.cloud.common.model.base.ResultEntity;

/**
 * 组织机构导入导出服务接口
 *
 * @author cloud
 */
public interface OrganizationImportService {

    /**
     * 下载导入模板
     *
     * @param response HTTP响应
     */
    void downloadTemplate(HttpServletResponse response);

    /**
     * 导入组织机构数据
     *
     * @param file Excel文件
     * @return 导入结果
     */
    ResultEntity<OrganizationImportResponse> importData(MultipartFile file);
}
