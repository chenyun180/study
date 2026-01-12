package com.cloud.admin.service;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.admin.model.dto.req.CompanySaveRequest;
import com.cloud.admin.model.dto.req.DepartmentSaveRequest;
import com.cloud.admin.model.dto.req.OrgUserSaveRequest;
import com.cloud.admin.model.dto.req.OrganizationDeleteRequest;
import com.cloud.admin.model.dto.req.OrganizationListRequest;
import com.cloud.admin.model.dto.req.OrganizationTreeRequest;
import com.cloud.admin.model.dto.req.PositionSaveRequest;
import com.cloud.admin.model.dto.rsp.CompanyDetailResponse;
import com.cloud.admin.model.dto.rsp.DepartmentDetailResponse;
import com.cloud.admin.model.dto.rsp.OrgUserDetailResponse;
import com.cloud.admin.model.dto.rsp.OrganizationListItem;
import com.cloud.admin.model.dto.rsp.OrganizationTreeNode;
import com.cloud.admin.model.dto.rsp.PositionDetailResponse;
import com.cloud.admin.model.dto.rsp.SaveIdResponse;
import com.cloud.common.model.base.ResultEntity;

/**
 * 组织管理服务接口
 *
 * @author cloud
 */
public interface OrganizationService {

    /**
     * 获取组织机构树
     *
     * @param request 请求参数
     * @return 组织机构树
     */
    ResultEntity<List<OrganizationTreeNode>> getOrganizationTree(OrganizationTreeRequest request);

    /**
     * 获取组织机构列表
     *
     * @param request 请求参数
     * @return 分页列表
     */
    ResultEntity<IPage<OrganizationListItem>> getOrganizationList(OrganizationListRequest request);

    /**
     * 获取公司详情
     *
     * @param id 公司ID
     * @return 公司详情
     */
    ResultEntity<CompanyDetailResponse> getCompanyDetail(Long id);

    /**
     * 保存公司信息（新增或编辑）
     *
     * @param request 请求参数
     * @return 保存结果
     */
    ResultEntity<SaveIdResponse> saveCompany(CompanySaveRequest request);

    /**
     * 获取部门详情
     *
     * @param id 部门ID
     * @return 部门详情
     */
    ResultEntity<DepartmentDetailResponse> getDepartmentDetail(Long id);

    /**
     * 保存部门信息（新增或编辑）
     *
     * @param request 请求参数
     * @return 保存结果
     */
    ResultEntity<SaveIdResponse> saveDepartment(DepartmentSaveRequest request);

    /**
     * 获取岗位详情
     *
     * @param id 岗位ID
     * @return 岗位详情
     */
    ResultEntity<PositionDetailResponse> getPositionDetail(Long id);

    /**
     * 保存岗位信息（新增或编辑）
     *
     * @param request 请求参数
     * @return 保存结果
     */
    ResultEntity<SaveIdResponse> savePosition(PositionSaveRequest request);

    /**
     * 获取组织用户详情
     *
     * @param id 用户ID
     * @return 用户详情
     */
    ResultEntity<OrgUserDetailResponse> getOrgUserDetail(Long id);

    /**
     * 保存组织用户信息（新增或编辑）
     *
     * @param request 请求参数
     * @return 保存结果
     */
    ResultEntity<SaveIdResponse> saveOrgUser(OrgUserSaveRequest request);

    /**
     * 删除组织机构节点
     *
     * @param request 删除请求
     * @return 删除结果
     */
    ResultEntity<Void> deleteOrganization(OrganizationDeleteRequest request);
}
