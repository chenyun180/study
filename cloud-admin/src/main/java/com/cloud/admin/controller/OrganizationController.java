package com.cloud.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.admin.model.dto.req.*;
import com.cloud.admin.model.dto.rsp.*;
import com.cloud.admin.service.OrganizationImportService;
import com.cloud.admin.service.OrganizationService;
import com.cloud.common.model.base.ResultEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 组织机构管理控制器
 *
 * @author cloud
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/organization", produces = "application/json;charset=utf-8")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OrganizationImportService organizationImportService;

    /**
     * 获取组织机构树
     *
     * @param request 请求参数
     * @return 组织机构树
     */
    @PostMapping("/tree")
    public ResultEntity<List<OrganizationTreeNode>> getOrganizationTree(@RequestBody OrganizationTreeRequest request) {
        return organizationService.getOrganizationTree(request);
    }

    /**
     * 获取组织机构列表
     *
     * @param request 请求参数
     * @return 分页列表
     */
    @PostMapping("/list")
    public ResultEntity<IPage<OrganizationListItem>> getOrganizationList(@RequestBody OrganizationListRequest request) {
        return organizationService.getOrganizationList(request);
    }

    /**
     * 获取公司详情
     *
     * @param request 请求参数
     * @return 公司详情
     */
    @PostMapping("/company/detail")
    public ResultEntity<CompanyDetailResponse> getCompanyDetail(@RequestBody @Validated IdRequest request) {
        return organizationService.getCompanyDetail(request.getId());
    }

    /**
     * 新增/编辑公司
     *
     * @param request 请求参数
     * @return 保存结果
     */
    @PostMapping("/company/save")
    public ResultEntity<SaveIdResponse> saveCompany(@RequestBody @Validated CompanySaveRequest request) {
        return organizationService.saveCompany(request);
    }

    /**
     * 获取部门详情
     *
     * @param request 请求参数
     * @return 部门详情
     */
    @PostMapping("/department/detail")
    public ResultEntity<DepartmentDetailResponse> getDepartmentDetail(@RequestBody @Validated IdRequest request) {
        return organizationService.getDepartmentDetail(request.getId());
    }

    /**
     * 新增/编辑部门
     *
     * @param request 请求参数
     * @return 保存结果
     */
    @PostMapping("/department/save")
    public ResultEntity<SaveIdResponse> saveDepartment(@RequestBody @Validated DepartmentSaveRequest request) {
        return organizationService.saveDepartment(request);
    }

    /**
     * 获取岗位详情
     *
     * @param request 请求参数
     * @return 岗位详情
     */
    @PostMapping("/position/detail")
    public ResultEntity<PositionDetailResponse> getPositionDetail(@RequestBody @Validated IdRequest request) {
        return organizationService.getPositionDetail(request.getId());
    }

    /**
     * 新增/编辑岗位
     *
     * @param request 请求参数
     * @return 保存结果
     */
    @PostMapping("/position/save")
    public ResultEntity<SaveIdResponse> savePosition(@RequestBody @Validated PositionSaveRequest request) {
        return organizationService.savePosition(request);
    }

    /**
     * 获取组织用户详情
     *
     * @param request 请求参数
     * @return 用户详情
     */
    @PostMapping("/user/detail")
    public ResultEntity<OrgUserDetailResponse> getOrgUserDetail(@RequestBody @Validated IdRequest request) {
        return organizationService.getOrgUserDetail(request.getId());
    }

    /**
     * 新增/编辑组织用户
     *
     * @param request 请求参数
     * @return 保存结果
     */
    @PostMapping("/user/save")
    public ResultEntity<SaveIdResponse> saveOrgUser(@RequestBody @Validated OrgUserSaveRequest request) {
        return organizationService.saveOrgUser(request);
    }

    /**
     * 删除组织机构节点
     *
     * @param request 请求参数
     * @return 删除结果
     */
    @PostMapping("/delete")
    public ResultEntity<Void> deleteOrganization(@RequestBody @Validated OrganizationDeleteRequest request) {
        return organizationService.deleteOrganization(request);
    }

    /**
     * 下载导入模板
     *
     * @param response HTTP响应
     */
    @PostMapping("/template/download")
    public void downloadTemplate(HttpServletResponse response) {
        organizationImportService.downloadTemplate(response);
    }

    /**
     * 导入组织机构数据
     *
     * @param file Excel文件
     * @return 导入结果
     */
    @PostMapping("/import")
    public ResultEntity<OrganizationImportResponse> importData(MultipartFile file) {
        return organizationImportService.importData(file);
    }
}

