package com.cloud.admin.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloud.admin.mapper.SysCompanyMapper;
import com.cloud.admin.mapper.SysDepartmentMapper;
import com.cloud.admin.mapper.SysOrgUserMapper;
import com.cloud.admin.mapper.SysPositionMapper;
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
import com.cloud.admin.model.entity.SysCompany;
import com.cloud.admin.model.entity.SysDepartment;
import com.cloud.admin.model.entity.SysOrgUser;
import com.cloud.admin.model.entity.SysPosition;
import com.cloud.admin.service.OrganizationService;
import com.cloud.common.model.base.ResultEntity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 组织管理服务实现类
 *
 * @author cloud
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final SysCompanyMapper companyMapper;
    private final SysDepartmentMapper departmentMapper;
    private final SysPositionMapper positionMapper;
    private final SysOrgUserMapper orgUserMapper;

    /**
     * 状态转换：数据库状态值转换为API状态字符串
     *
     * @param status 数据库状态值（1-启用，0-停用）
     * @return API状态字符串（active/inactive）
     */
    private String convertStatusToString(Integer status) {
        return Integer.valueOf(1).equals(status) ? "active" : "inactive";
    }

    /**
     * 状态转换：API状态字符串转换为数据库状态值
     *
     * @param status API状态字符串（active/inactive）
     * @return 数据库状态值（1-启用，0-停用）
     */
    private Integer convertStatusToInt(String status) {
        return "active".equals(status) ? 1 : 0;
    }

    @Override
    public ResultEntity<List<OrganizationTreeNode>> getOrganizationTree(OrganizationTreeRequest request) {
        List<OrganizationTreeNode> treeNodes = new ArrayList<>();

        // 查询所有公司
        LambdaQueryWrapper<SysCompany> companyWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(request.getKeyword())) {
            companyWrapper.like(SysCompany::getName, request.getKeyword());
        }
        companyWrapper.orderByAsc(SysCompany::getSort);
        List<SysCompany> companies = companyMapper.selectList(companyWrapper);

        // 查询所有部门
        LambdaQueryWrapper<SysDepartment> departmentWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(request.getKeyword())) {
            departmentWrapper.like(SysDepartment::getName, request.getKeyword());
        }
        departmentWrapper.orderByAsc(SysDepartment::getSort);
        List<SysDepartment> departments = departmentMapper.selectList(departmentWrapper);

        // 查询所有岗位
        LambdaQueryWrapper<SysPosition> positionWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(request.getKeyword())) {
            positionWrapper.like(SysPosition::getName, request.getKeyword());
        }
        positionWrapper.orderByAsc(SysPosition::getSort);
        List<SysPosition> positions = positionMapper.selectList(positionWrapper);

        // 查询所有组织用户
        LambdaQueryWrapper<SysOrgUser> userWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(request.getKeyword())) {
            userWrapper.like(SysOrgUser::getName, request.getKeyword());
        }
        userWrapper.orderByAsc(SysOrgUser::getSort);
        List<SysOrgUser> users = orgUserMapper.selectList(userWrapper);

        // 构建公司ID到名称的映射
        Map<Long, String> companyNameMap = companies.stream()
                .collect(Collectors.toMap(SysCompany::getId, SysCompany::getName));
        // 构建部门ID到名称的映射
        Map<Long, String> departmentNameMap = departments.stream()
                .collect(Collectors.toMap(SysDepartment::getId, SysDepartment::getName));
        // 构建岗位ID到名称的映射
        Map<Long, String> positionNameMap = positions.stream()
                .collect(Collectors.toMap(SysPosition::getId, SysPosition::getName));

        // 构建用户节点（按岗位分组）
        Map<Long, List<OrganizationTreeNode>> userNodesByPosition = users.stream()
                .collect(Collectors.groupingBy(
                        SysOrgUser::getPositionId,
                        Collectors.mapping(user -> {
                            String positionName = positionNameMap.getOrDefault(user.getPositionId(), "");
                            return OrganizationTreeNode.builder()
                                    .id(user.getId())
                                    .parentId(user.getPositionId())
                                    .code(user.getCode())
                                    .name(user.getName())
                                    .type("user")
                                    .path(positionName + "/" + user.getName())
                                    .status(convertStatusToString(user.getStatus()))
                                    .sort(user.getSort())
                                    .children(new ArrayList<>())
                                    .build();
                        }, Collectors.toList())
                ));

        // 构建岗位节点（按部门分组）
        Map<Long, List<OrganizationTreeNode>> positionNodesByDepartment = positions.stream()
                .collect(Collectors.groupingBy(
                        SysPosition::getDepartmentId,
                        Collectors.mapping(position -> {
                            String deptName = departmentNameMap.getOrDefault(position.getDepartmentId(), "");
                            OrganizationTreeNode node = OrganizationTreeNode.builder()
                                    .id(position.getId())
                                    .parentId(position.getDepartmentId())
                                    .code(position.getCode())
                                    .name(position.getName())
                                    .type("position")
                                    .path(deptName + "/" + position.getName())
                                    .status(convertStatusToString(position.getStatus()))
                                    .sort(position.getSort())
                                    .children(userNodesByPosition.getOrDefault(position.getId(), new ArrayList<>()))
                                    .build();
                            return node;
                        }, Collectors.toList())
                ));

        // 构建部门节点（按公司分组）
        Map<Long, List<OrganizationTreeNode>> departmentNodesByCompany = departments.stream()
                .collect(Collectors.groupingBy(
                        SysDepartment::getCompanyId,
                        Collectors.mapping(department -> {
                            String companyName = companyNameMap.getOrDefault(department.getCompanyId(), "");
                            OrganizationTreeNode node = OrganizationTreeNode.builder()
                                    .id(department.getId())
                                    .parentId(department.getCompanyId())
                                    .code(department.getCode())
                                    .name(department.getName())
                                    .type("department")
                                    .path(companyName + "/" + department.getName())
                                    .status(convertStatusToString(department.getStatus()))
                                    .sort(department.getSort())
                                    .children(positionNodesByDepartment.getOrDefault(department.getId(), new ArrayList<>()))
                                    .build();
                            return node;
                        }, Collectors.toList())
                ));

        // 构建公司节点
        // 先处理子公司关系
        Map<Long, List<SysCompany>> childCompaniesMap = companies.stream()
                .filter(c -> c.getParentId() != null)
                .collect(Collectors.groupingBy(SysCompany::getParentId));

        // 递归构建公司树
        List<SysCompany> rootCompanies = companies.stream()
                .filter(c -> c.getParentId() == null)
                .collect(Collectors.toList());

        for (SysCompany company : rootCompanies) {
            OrganizationTreeNode node = buildCompanyTreeNode(company, childCompaniesMap, 
                    departmentNodesByCompany, companyNameMap, company.getName());
            treeNodes.add(node);
        }

        return ResultEntity.ok(treeNodes, "获取组织机构树成功");
    }

    /**
     * 递归构建公司树节点
     *
     * @param company                   公司实体
     * @param childCompaniesMap         子公司映射
     * @param departmentNodesByCompany  部门节点按公司分组
     * @param companyNameMap            公司名称映射
     * @param path                      当前路径
     * @return 公司树节点
     */
    private OrganizationTreeNode buildCompanyTreeNode(SysCompany company,
                                                      Map<Long, List<SysCompany>> childCompaniesMap,
                                                      Map<Long, List<OrganizationTreeNode>> departmentNodesByCompany,
                                                      Map<Long, String> companyNameMap,
                                                      String path) {
        List<OrganizationTreeNode> children = new ArrayList<>();

        // 添加子公司
        List<SysCompany> childCompanies = childCompaniesMap.get(company.getId());
        if (CollUtil.isNotEmpty(childCompanies)) {
            for (SysCompany childCompany : childCompanies) {
                String childPath = path + "/" + childCompany.getName();
                children.add(buildCompanyTreeNode(childCompany, childCompaniesMap, 
                        departmentNodesByCompany, companyNameMap, childPath));
            }
        }

        // 添加部门
        List<OrganizationTreeNode> departmentNodes = departmentNodesByCompany.get(company.getId());
        if (CollUtil.isNotEmpty(departmentNodes)) {
            // 更新部门路径
            for (OrganizationTreeNode deptNode : departmentNodes) {
                deptNode.setPath(path + "/" + deptNode.getName());
                // 更新岗位路径
                if (CollUtil.isNotEmpty(deptNode.getChildren())) {
                    for (OrganizationTreeNode posNode : deptNode.getChildren()) {
                        posNode.setPath(deptNode.getPath() + "/" + posNode.getName());
                        // 更新用户路径
                        if (CollUtil.isNotEmpty(posNode.getChildren())) {
                            for (OrganizationTreeNode userNode : posNode.getChildren()) {
                                userNode.setPath(posNode.getPath() + "/" + userNode.getName());
                            }
                        }
                    }
                }
            }
            children.addAll(departmentNodes);
        }

        return OrganizationTreeNode.builder()
                .id(company.getId())
                .parentId(company.getParentId())
                .code(company.getCode())
                .name(company.getName())
                .type("company")
                .path(path)
                .status(convertStatusToString(company.getStatus()))
                .sort(company.getSort())
                .children(children)
                .build();
    }

    @Override
    public ResultEntity<IPage<OrganizationListItem>> getOrganizationList(OrganizationListRequest request) {
        String type = request.getType();
        Page<OrganizationListItem> resultPage = new Page<>(request.getCurrent(), request.getSize());
        List<OrganizationListItem> records = new ArrayList<>();
        long total = 0;

        if (StrUtil.isBlank(type) || "company".equals(type)) {
            // 查询公司
            Page<SysCompany> companyPage = new Page<>(request.getCurrent(), request.getSize());
            LambdaQueryWrapper<SysCompany> wrapper = new LambdaQueryWrapper<>();
            if (request.getParentId() != null) {
                wrapper.eq(SysCompany::getParentId, request.getParentId());
            }
            if (StrUtil.isNotBlank(request.getCode())) {
                wrapper.like(SysCompany::getCode, request.getCode());
            }
            if (StrUtil.isNotBlank(request.getName())) {
                wrapper.like(SysCompany::getName, request.getName());
            }
            wrapper.orderByAsc(SysCompany::getSort);
            IPage<SysCompany> companyResult = companyMapper.selectPage(companyPage, wrapper);

            for (SysCompany company : companyResult.getRecords()) {
                records.add(OrganizationListItem.builder()
                        .id(company.getId())
                        .type("company")
                        .code(company.getCode())
                        .name(company.getName())
                        .path(buildCompanyPath(company.getId()))
                        .status(convertStatusToString(company.getStatus()))
                        .createTime(company.getCreateTime())
                        .build());
            }
            total = companyResult.getTotal();
        } else if ("department".equals(type)) {
            // 查询部门
            Page<SysDepartment> departmentPage = new Page<>(request.getCurrent(), request.getSize());
            LambdaQueryWrapper<SysDepartment> wrapper = new LambdaQueryWrapper<>();
            if (request.getParentId() != null) {
                wrapper.eq(SysDepartment::getCompanyId, request.getParentId());
            }
            if (StrUtil.isNotBlank(request.getCode())) {
                wrapper.like(SysDepartment::getCode, request.getCode());
            }
            if (StrUtil.isNotBlank(request.getName())) {
                wrapper.like(SysDepartment::getName, request.getName());
            }
            wrapper.orderByAsc(SysDepartment::getSort);
            IPage<SysDepartment> departmentResult = departmentMapper.selectPage(departmentPage, wrapper);

            // 批量查询公司名称
            Set<Long> companyIds = departmentResult.getRecords().stream()
                    .map(SysDepartment::getCompanyId)
                    .collect(Collectors.toSet());
            Map<Long, String> companyNameMap = new HashMap<>();
            if (CollUtil.isNotEmpty(companyIds)) {
                List<SysCompany> companies = companyMapper.selectBatchIds(companyIds);
                companyNameMap = companies.stream()
                        .collect(Collectors.toMap(SysCompany::getId, SysCompany::getName));
            }

            for (SysDepartment department : departmentResult.getRecords()) {
                String companyName = companyNameMap.getOrDefault(department.getCompanyId(), "");
                records.add(OrganizationListItem.builder()
                        .id(department.getId())
                        .type("department")
                        .code(department.getCode())
                        .name(department.getName())
                        .path(companyName + "/" + department.getName())
                        .status(convertStatusToString(department.getStatus()))
                        .createTime(department.getCreateTime())
                        .build());
            }
            total = departmentResult.getTotal();
        } else if ("position".equals(type)) {
            // 查询岗位
            Page<SysPosition> positionPage = new Page<>(request.getCurrent(), request.getSize());
            LambdaQueryWrapper<SysPosition> wrapper = new LambdaQueryWrapper<>();
            if (request.getParentId() != null) {
                wrapper.eq(SysPosition::getDepartmentId, request.getParentId());
            }
            if (StrUtil.isNotBlank(request.getCode())) {
                wrapper.like(SysPosition::getCode, request.getCode());
            }
            if (StrUtil.isNotBlank(request.getName())) {
                wrapper.like(SysPosition::getName, request.getName());
            }
            wrapper.orderByAsc(SysPosition::getSort);
            IPage<SysPosition> positionResult = positionMapper.selectPage(positionPage, wrapper);

            // 批量查询部门和公司信息
            Set<Long> departmentIds = positionResult.getRecords().stream()
                    .map(SysPosition::getDepartmentId)
                    .collect(Collectors.toSet());
            Map<Long, SysDepartment> departmentMap = new HashMap<>();
            Set<Long> companyIds = new HashSet<>();
            if (CollUtil.isNotEmpty(departmentIds)) {
                List<SysDepartment> departments = departmentMapper.selectBatchIds(departmentIds);
                departmentMap = departments.stream()
                        .collect(Collectors.toMap(SysDepartment::getId, d -> d));
                companyIds = departments.stream()
                        .map(SysDepartment::getCompanyId)
                        .collect(Collectors.toSet());
            }
            Map<Long, String> companyNameMap = new HashMap<>();
            if (CollUtil.isNotEmpty(companyIds)) {
                List<SysCompany> companies = companyMapper.selectBatchIds(companyIds);
                companyNameMap = companies.stream()
                        .collect(Collectors.toMap(SysCompany::getId, SysCompany::getName));
            }

            for (SysPosition position : positionResult.getRecords()) {
                SysDepartment dept = departmentMap.get(position.getDepartmentId());
                String path = "";
                if (dept != null) {
                    String companyName = companyNameMap.getOrDefault(dept.getCompanyId(), "");
                    path = companyName + "/" + dept.getName() + "/" + position.getName();
                }
                records.add(OrganizationListItem.builder()
                        .id(position.getId())
                        .type("position")
                        .code(position.getCode())
                        .name(position.getName())
                        .path(path)
                        .status(convertStatusToString(position.getStatus()))
                        .createTime(position.getCreateTime())
                        .build());
            }
            total = positionResult.getTotal();
        } else if ("user".equals(type)) {
            // 查询用户
            Page<SysOrgUser> userPage = new Page<>(request.getCurrent(), request.getSize());
            LambdaQueryWrapper<SysOrgUser> wrapper = new LambdaQueryWrapper<>();
            if (request.getParentId() != null) {
                wrapper.eq(SysOrgUser::getPositionId, request.getParentId());
            }
            if (StrUtil.isNotBlank(request.getCode())) {
                wrapper.like(SysOrgUser::getCode, request.getCode());
            }
            if (StrUtil.isNotBlank(request.getName())) {
                wrapper.like(SysOrgUser::getName, request.getName());
            }
            wrapper.orderByAsc(SysOrgUser::getSort);
            IPage<SysOrgUser> userResult = orgUserMapper.selectPage(userPage, wrapper);

            // 批量查询岗位、部门、公司信息
            Set<Long> positionIds = userResult.getRecords().stream()
                    .map(SysOrgUser::getPositionId)
                    .collect(Collectors.toSet());
            Map<Long, SysPosition> positionMap = new HashMap<>();
            Set<Long> departmentIds = new HashSet<>();
            if (CollUtil.isNotEmpty(positionIds)) {
                List<SysPosition> positions = positionMapper.selectBatchIds(positionIds);
                positionMap = positions.stream()
                        .collect(Collectors.toMap(SysPosition::getId, p -> p));
                departmentIds = positions.stream()
                        .map(SysPosition::getDepartmentId)
                        .collect(Collectors.toSet());
            }
            Map<Long, SysDepartment> departmentMap = new HashMap<>();
            Set<Long> companyIds = new HashSet<>();
            if (CollUtil.isNotEmpty(departmentIds)) {
                List<SysDepartment> departments = departmentMapper.selectBatchIds(departmentIds);
                departmentMap = departments.stream()
                        .collect(Collectors.toMap(SysDepartment::getId, d -> d));
                companyIds = departments.stream()
                        .map(SysDepartment::getCompanyId)
                        .collect(Collectors.toSet());
            }
            Map<Long, String> companyNameMap = new HashMap<>();
            if (CollUtil.isNotEmpty(companyIds)) {
                List<SysCompany> companies = companyMapper.selectBatchIds(companyIds);
                companyNameMap = companies.stream()
                        .collect(Collectors.toMap(SysCompany::getId, SysCompany::getName));
            }

            for (SysOrgUser user : userResult.getRecords()) {
                String path = "";
                SysPosition pos = positionMap.get(user.getPositionId());
                if (pos != null) {
                    SysDepartment dept = departmentMap.get(pos.getDepartmentId());
                    if (dept != null) {
                        String companyName = companyNameMap.getOrDefault(dept.getCompanyId(), "");
                        path = companyName + "/" + dept.getName() + "/" + pos.getName() + "/" + user.getName();
                    }
                }
                records.add(OrganizationListItem.builder()
                        .id(user.getId())
                        .type("user")
                        .code(user.getCode())
                        .name(user.getName())
                        .path(path)
                        .status(convertStatusToString(user.getStatus()))
                        .createTime(user.getCreateTime())
                        .build());
            }
            total = userResult.getTotal();
        }

        resultPage.setRecords(records);
        resultPage.setTotal(total);
        return ResultEntity.ok(resultPage, "获取组织机构列表成功");
    }

    /**
     * 构建公司完整路径
     *
     * @param companyId 公司ID
     * @return 完整路径
     */
    private String buildCompanyPath(Long companyId) {
        List<String> pathParts = new ArrayList<>();
        Long currentId = companyId;

        while (currentId != null) {
            SysCompany company = companyMapper.selectById(currentId);
            if (company == null) {
                break;
            }
            pathParts.add(0, company.getName());
            currentId = company.getParentId();
        }

        return String.join("/", pathParts);
    }

    @Override
    public ResultEntity<CompanyDetailResponse> getCompanyDetail(Long id) {
        SysCompany company = companyMapper.selectById(id);
        if (company == null) {
            return ResultEntity.failed("公司不存在");
        }

        CompanyDetailResponse response = CompanyDetailResponse.builder()
                .id(company.getId())
                .parentId(company.getParentId())
                .code(company.getCode())
                .name(company.getName())
                .shortName(company.getShortName())
                .address(company.getAddress())
                .phone(company.getPhone())
                .email(company.getEmail())
                .legalPerson(company.getLegalPerson())
                .status(convertStatusToString(company.getStatus()))
                .sort(company.getSort())
                .remark(company.getRemark())
                .createTime(company.getCreateTime())
                .updateTime(company.getUpdateTime())
                .build();

        return ResultEntity.ok(response, "获取公司详情成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity<SaveIdResponse> saveCompany(CompanySaveRequest request) {
        // 检查编码是否重复
        LambdaQueryWrapper<SysCompany> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysCompany::getCode, request.getCode());
        if (request.getId() != null) {
            wrapper.ne(SysCompany::getId, request.getId());
        }
        int count = companyMapper.selectCount(wrapper);
        if (count > 0) {
            return ResultEntity.failed("公司编码已存在");
        }

        SysCompany company = SysCompany.builder()
                .id(request.getId())
                .parentId(request.getParentId())
                .code(request.getCode())
                .name(request.getName())
                .shortName(request.getShortName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .legalPerson(request.getLegalPerson())
                .status(convertStatusToInt(request.getStatus()))
                .sort(request.getSort())
                .remark(request.getRemark())
                .build();

        if (request.getId() == null) {
            companyMapper.insert(company);
        } else {
            companyMapper.updateById(company);
        }

        return ResultEntity.ok(SaveIdResponse.builder().id(company.getId()).build(), "保存成功");
    }

    @Override
    public ResultEntity<DepartmentDetailResponse> getDepartmentDetail(Long id) {
        SysDepartment department = departmentMapper.selectById(id);
        if (department == null) {
            return ResultEntity.failed("部门不存在");
        }

        // 查询负责人姓名
        String leaderName = null;
        if (department.getLeaderId() != null) {
            SysOrgUser leader = orgUserMapper.selectById(department.getLeaderId());
            if (leader != null) {
                leaderName = leader.getName();
            }
        }

        DepartmentDetailResponse response = DepartmentDetailResponse.builder()
                .id(department.getId())
                .parentId(department.getParentId())
                .companyId(department.getCompanyId())
                .code(department.getCode())
                .name(department.getName())
                .leaderId(department.getLeaderId())
                .leaderName(leaderName)
                .phone(department.getPhone())
                .email(department.getEmail())
                .status(convertStatusToString(department.getStatus()))
                .sort(department.getSort())
                .remark(department.getRemark())
                .createTime(department.getCreateTime())
                .updateTime(department.getUpdateTime())
                .build();

        return ResultEntity.ok(response, "获取部门详情成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity<SaveIdResponse> saveDepartment(DepartmentSaveRequest request) {
        // 检查公司是否存在
        SysCompany company = companyMapper.selectById(request.getCompanyId());
        if (company == null) {
            return ResultEntity.failed("所属公司不存在");
        }

        // 检查编码是否重复
        LambdaQueryWrapper<SysDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDepartment::getCode, request.getCode());
        if (request.getId() != null) {
            wrapper.ne(SysDepartment::getId, request.getId());
        }
        int count = departmentMapper.selectCount(wrapper);
        if (count > 0) {
            return ResultEntity.failed("部门编码已存在");
        }

        SysDepartment department = SysDepartment.builder()
                .id(request.getId())
                .parentId(request.getParentId())
                .companyId(request.getCompanyId())
                .code(request.getCode())
                .name(request.getName())
                .leaderId(request.getLeaderId())
                .phone(request.getPhone())
                .email(request.getEmail())
                .status(convertStatusToInt(request.getStatus()))
                .sort(request.getSort())
                .remark(request.getRemark())
                .build();

        if (request.getId() == null) {
            departmentMapper.insert(department);
        } else {
            departmentMapper.updateById(department);
        }

        return ResultEntity.ok(SaveIdResponse.builder().id(department.getId()).build(), "保存成功");
    }

    @Override
    public ResultEntity<PositionDetailResponse> getPositionDetail(Long id) {
        SysPosition position = positionMapper.selectById(id);
        if (position == null) {
            return ResultEntity.failed("岗位不存在");
        }

        PositionDetailResponse response = PositionDetailResponse.builder()
                .id(position.getId())
                .parentId(position.getParentId())
                .departmentId(position.getDepartmentId())
                .code(position.getCode())
                .name(position.getName())
                .level(position.getLevel())
                .status(convertStatusToString(position.getStatus()))
                .sort(position.getSort())
                .remark(position.getRemark())
                .createTime(position.getCreateTime())
                .updateTime(position.getUpdateTime())
                .build();

        return ResultEntity.ok(response, "获取岗位详情成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity<SaveIdResponse> savePosition(PositionSaveRequest request) {
        // 检查部门是否存在
        SysDepartment department = departmentMapper.selectById(request.getDepartmentId());
        if (department == null) {
            return ResultEntity.failed("所属部门不存在");
        }

        // 检查编码是否重复
        LambdaQueryWrapper<SysPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysPosition::getCode, request.getCode());
        if (request.getId() != null) {
            wrapper.ne(SysPosition::getId, request.getId());
        }
        int count = positionMapper.selectCount(wrapper);
        if (count > 0) {
            return ResultEntity.failed("岗位编码已存在");
        }

        SysPosition position = SysPosition.builder()
                .id(request.getId())
                .parentId(request.getParentId())
                .departmentId(request.getDepartmentId())
                .code(request.getCode())
                .name(request.getName())
                .level(request.getLevel())
                .status(convertStatusToInt(request.getStatus()))
                .sort(request.getSort())
                .remark(request.getRemark())
                .build();

        if (request.getId() == null) {
            positionMapper.insert(position);
        } else {
            positionMapper.updateById(position);
        }

        return ResultEntity.ok(SaveIdResponse.builder().id(position.getId()).build(), "保存成功");
    }

    @Override
    public ResultEntity<OrgUserDetailResponse> getOrgUserDetail(Long id) {
        SysOrgUser user = orgUserMapper.selectById(id);
        if (user == null) {
            return ResultEntity.failed("用户不存在");
        }

        OrgUserDetailResponse response = OrgUserDetailResponse.builder()
                .id(user.getId())
                .positionId(user.getPositionId())
                .code(user.getCode())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .status(convertStatusToString(user.getStatus()))
                .sort(user.getSort())
                .remark(user.getRemark())
                .createTime(user.getCreateTime())
                .updateTime(user.getUpdateTime())
                .build();

        return ResultEntity.ok(response, "获取用户详情成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity<SaveIdResponse> saveOrgUser(OrgUserSaveRequest request) {
        // 检查岗位是否存在
        SysPosition position = positionMapper.selectById(request.getPositionId());
        if (position == null) {
            return ResultEntity.failed("所属岗位不存在");
        }

        // 检查编码是否重复
        LambdaQueryWrapper<SysOrgUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysOrgUser::getCode, request.getCode());
        if (request.getId() != null) {
            wrapper.ne(SysOrgUser::getId, request.getId());
        }
        int count = orgUserMapper.selectCount(wrapper);
        if (count > 0) {
            return ResultEntity.failed("用户编码已存在");
        }

        SysOrgUser user = SysOrgUser.builder()
                .id(request.getId())
                .positionId(request.getPositionId())
                .code(request.getCode())
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .status(convertStatusToInt(request.getStatus()))
                .sort(request.getSort())
                .remark(request.getRemark())
                .build();

        if (request.getId() == null) {
            orgUserMapper.insert(user);
        } else {
            orgUserMapper.updateById(user);
        }

        return ResultEntity.ok(SaveIdResponse.builder().id(user.getId()).build(), "保存成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity<Void> deleteOrganization(OrganizationDeleteRequest request) {
        String type = request.getType();
        Long id = request.getId();

        switch (type) {
            case "company":
                return deleteCompany(id);
            case "department":
                return deleteDepartment(id);
            case "position":
                return deletePosition(id);
            case "user":
                return deleteOrgUser(id);
            default:
                return ResultEntity.failed("不支持的节点类型");
        }
    }

    /**
     * 删除公司
     *
     * @param id 公司ID
     * @return 删除结果
     */
    private ResultEntity<Void> deleteCompany(Long id) {
        // 检查是否存在子公司
        LambdaQueryWrapper<SysCompany> companyWrapper = new LambdaQueryWrapper<>();
        companyWrapper.eq(SysCompany::getParentId, id);
        int childCompanyCount = companyMapper.selectCount(companyWrapper);
        if (childCompanyCount > 0) {
            return ResultEntity.failed("该公司下存在子公司，无法删除");
        }

        // 检查是否存在部门
        LambdaQueryWrapper<SysDepartment> departmentWrapper = new LambdaQueryWrapper<>();
        departmentWrapper.eq(SysDepartment::getCompanyId, id);
        int departmentCount = departmentMapper.selectCount(departmentWrapper);
        if (departmentCount > 0) {
            return ResultEntity.failed("该公司下存在部门，无法删除");
        }

        companyMapper.deleteById(id);
        return ResultEntity.ok(null, "删除成功");
    }

    /**
     * 删除部门
     *
     * @param id 部门ID
     * @return 删除结果
     */
    private ResultEntity<Void> deleteDepartment(Long id) {
        // 检查是否存在子部门
        LambdaQueryWrapper<SysDepartment> departmentWrapper = new LambdaQueryWrapper<>();
        departmentWrapper.eq(SysDepartment::getParentId, id);
        int childDepartmentCount = departmentMapper.selectCount(departmentWrapper);
        if (childDepartmentCount > 0) {
            return ResultEntity.failed("该部门下存在子部门，无法删除");
        }

        // 检查是否存在岗位
        LambdaQueryWrapper<SysPosition> positionWrapper = new LambdaQueryWrapper<>();
        positionWrapper.eq(SysPosition::getDepartmentId, id);
        int positionCount = positionMapper.selectCount(positionWrapper);
        if (positionCount > 0) {
            return ResultEntity.failed("该部门下存在岗位，无法删除");
        }

        departmentMapper.deleteById(id);
        return ResultEntity.ok(null, "删除成功");
    }

    /**
     * 删除岗位
     *
     * @param id 岗位ID
     * @return 删除结果
     */
    private ResultEntity<Void> deletePosition(Long id) {
        // 检查是否存在子岗位
        LambdaQueryWrapper<SysPosition> positionWrapper = new LambdaQueryWrapper<>();
        positionWrapper.eq(SysPosition::getParentId, id);
        int childPositionCount = positionMapper.selectCount(positionWrapper);
        if (childPositionCount > 0) {
            return ResultEntity.failed("该岗位下存在子岗位，无法删除");
        }

        // 检查是否存在用户
        LambdaQueryWrapper<SysOrgUser> userWrapper = new LambdaQueryWrapper<>();
        userWrapper.eq(SysOrgUser::getPositionId, id);
        int userCount = orgUserMapper.selectCount(userWrapper);
        if (userCount > 0) {
            return ResultEntity.failed("该岗位下存在用户，无法删除");
        }

        positionMapper.deleteById(id);
        return ResultEntity.ok(null, "删除成功");
    }

    /**
     * 删除组织用户
     *
     * @param id 用户ID
     * @return 删除结果
     */
    private ResultEntity<Void> deleteOrgUser(Long id) {
        orgUserMapper.deleteById(id);
        return ResultEntity.ok(null, "删除成功");
    }
}

