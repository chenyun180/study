package com.cloud.admin.service.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cloud.admin.mapper.SysCompanyMapper;
import com.cloud.admin.mapper.SysDepartmentMapper;
import com.cloud.admin.mapper.SysOrgUserMapper;
import com.cloud.admin.mapper.SysPositionMapper;
import com.cloud.admin.model.dto.rsp.OrganizationImportResponse;
import com.cloud.admin.model.entity.SysCompany;
import com.cloud.admin.model.entity.SysDepartment;
import com.cloud.admin.model.entity.SysOrgUser;
import com.cloud.admin.model.entity.SysPosition;
import com.cloud.admin.service.OrganizationImportService;
import com.cloud.common.model.base.ResultEntity;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 组织机构导入导出服务实现类
 *
 * @author cloud
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationImportServiceImpl implements OrganizationImportService {

    private final SysCompanyMapper companyMapper;
    private final SysDepartmentMapper departmentMapper;
    private final SysPositionMapper positionMapper;
    private final SysOrgUserMapper orgUserMapper;

    @Override
    public void downloadTemplate(HttpServletResponse response) {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("组织机构导入模板", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 创建模板数据
            List<List<String>> head = new ArrayList<>();
            head.add(Collections.singletonList("类型(company/department/position/user)"));
            head.add(Collections.singletonList("上级编码"));
            head.add(Collections.singletonList("编码"));
            head.add(Collections.singletonList("名称"));
            head.add(Collections.singletonList("状态(active/inactive)"));
            head.add(Collections.singletonList("排序号"));
            head.add(Collections.singletonList("备注"));

            // 示例数据
            List<List<String>> data = new ArrayList<>();
            data.add(Arrays.asList("company", "", "RQ-TEST", "测试公司", "active", "1", "测试公司备注"));
            data.add(Arrays.asList("department", "RQ-TEST", "RQ-TEST-RD", "研发部", "active", "1", ""));
            data.add(Arrays.asList("position", "RQ-TEST-RD", "RQ-TEST-RD-DEV", "开发岗位", "active", "1", ""));
            data.add(Arrays.asList("user", "RQ-TEST-RD-DEV", "U-TEST-001", "测试用户", "active", "1", ""));

            EasyExcel.write(response.getOutputStream())
                    .head(head)
                    .sheet("组织机构导入模板")
                    .doWrite(data);
        } catch (IOException e) {
            log.error("下载模板失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultEntity<OrganizationImportResponse> importData(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        int[] successCount = {0};
        int[] failCount = {0};

        try {
            EasyExcel.read(file.getInputStream(), new AnalysisEventListener<Map<Integer, String>>() {
                private int rowIndex = 1;

                @Override
                public void invoke(Map<Integer, String> data, AnalysisContext context) {
                    rowIndex++;
                    try {
                        processRow(data, rowIndex, errors);
                        successCount[0]++;
                    } catch (Exception e) {
                        errors.add("第" + rowIndex + "行：" + e.getMessage());
                        failCount[0]++;
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    log.info("导入完成，成功：{}，失败：{}", successCount[0], failCount[0]);
                }
            }).sheet().headRowNumber(1).doRead();
        } catch (IOException e) {
            log.error("导入文件读取失败", e);
            return ResultEntity.failed("导入文件读取失败：" + e.getMessage());
        }

        OrganizationImportResponse response = OrganizationImportResponse.builder()
                .success(successCount[0])
                .fail(failCount[0])
                .errors(errors)
                .build();

        return ResultEntity.ok(response, "导入完成");
    }

    /**
     * 处理导入行数据
     *
     * @param data     行数据
     * @param rowIndex 行号
     * @param errors   错误列表
     */
    private void processRow(Map<Integer, String> data, int rowIndex, List<String> errors) {
        String type = data.get(0);
        String parentCode = data.get(1);
        String code = data.get(2);
        String name = data.get(3);
        String status = data.get(4);
        String sortStr = data.get(5);
        String remark = data.get(6);

        if (StrUtil.isBlank(type)) {
            throw new RuntimeException("类型不能为空");
        }
        if (StrUtil.isBlank(code)) {
            throw new RuntimeException("编码不能为空");
        }
        if (StrUtil.isBlank(name)) {
            throw new RuntimeException("名称不能为空");
        }

        int sort = 1;
        if (StrUtil.isNotBlank(sortStr)) {
            try {
                sort = Integer.parseInt(sortStr);
            } catch (NumberFormatException e) {
                throw new RuntimeException("排序号格式错误");
            }
        }

        int statusValue = "inactive".equals(status) ? 0 : 1;

        switch (type) {
            case "company":
                importCompany(parentCode, code, name, statusValue, sort, remark);
                break;
            case "department":
                importDepartment(parentCode, code, name, statusValue, sort, remark);
                break;
            case "position":
                importPosition(parentCode, code, name, statusValue, sort, remark);
                break;
            case "user":
                importUser(parentCode, code, name, statusValue, sort, remark);
                break;
            default:
                throw new RuntimeException("不支持的类型：" + type);
        }
    }

    /**
     * 导入公司
     *
     * @param parentCode 上级编码
     * @param code       编码
     * @param name       名称
     * @param status     状态
     * @param sort       排序号
     * @param remark     备注
     */
    private void importCompany(String parentCode, String code, String name, int status, int sort, String remark) {
        // 检查编码是否存在
        LambdaQueryWrapper<SysCompany> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysCompany::getCode, code);
        int count = companyMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RuntimeException("公司编码已存在");
        }

        Long parentId = null;
        if (StrUtil.isNotBlank(parentCode)) {
            LambdaQueryWrapper<SysCompany> parentWrapper = new LambdaQueryWrapper<>();
            parentWrapper.eq(SysCompany::getCode, parentCode);
            SysCompany parent = companyMapper.selectOne(parentWrapper);
            if (parent == null) {
                throw new RuntimeException("上级公司不存在");
            }
            parentId = parent.getId();
        }

        SysCompany company = SysCompany.builder()
                .parentId(parentId)
                .code(code)
                .name(name)
                .status(status)
                .sort(sort)
                .remark(remark)
                .build();
        companyMapper.insert(company);
    }

    /**
     * 导入部门
     *
     * @param parentCode 上级编码
     * @param code       编码
     * @param name       名称
     * @param status     状态
     * @param sort       排序号
     * @param remark     备注
     */
    private void importDepartment(String parentCode, String code, String name, int status, int sort, String remark) {
        // 检查编码是否存在
        LambdaQueryWrapper<SysDepartment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDepartment::getCode, code);
        int count = departmentMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RuntimeException("部门编码已存在");
        }

        if (StrUtil.isBlank(parentCode)) {
            throw new RuntimeException("部门必须指定上级编码（公司或上级部门）");
        }

        // 先尝试查找公司
        LambdaQueryWrapper<SysCompany> companyWrapper = new LambdaQueryWrapper<>();
        companyWrapper.eq(SysCompany::getCode, parentCode);
        SysCompany company = companyMapper.selectOne(companyWrapper);

        Long companyId;
        Long parentId = null;

        if (company != null) {
            companyId = company.getId();
        } else {
            // 查找上级部门
            LambdaQueryWrapper<SysDepartment> deptWrapper = new LambdaQueryWrapper<>();
            deptWrapper.eq(SysDepartment::getCode, parentCode);
            SysDepartment parentDept = departmentMapper.selectOne(deptWrapper);
            if (parentDept == null) {
                throw new RuntimeException("上级部门或公司不存在");
            }
            companyId = parentDept.getCompanyId();
            parentId = parentDept.getId();
        }

        SysDepartment department = SysDepartment.builder()
                .parentId(parentId)
                .companyId(companyId)
                .code(code)
                .name(name)
                .status(status)
                .sort(sort)
                .remark(remark)
                .build();
        departmentMapper.insert(department);
    }

    /**
     * 导入岗位
     *
     * @param parentCode 上级编码
     * @param code       编码
     * @param name       名称
     * @param status     状态
     * @param sort       排序号
     * @param remark     备注
     */
    private void importPosition(String parentCode, String code, String name, int status, int sort, String remark) {
        // 检查编码是否存在
        LambdaQueryWrapper<SysPosition> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysPosition::getCode, code);
        int count = positionMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RuntimeException("岗位编码已存在");
        }

        if (StrUtil.isBlank(parentCode)) {
            throw new RuntimeException("岗位必须指定上级编码（部门或上级岗位）");
        }

        // 先尝试查找部门
        LambdaQueryWrapper<SysDepartment> deptWrapper = new LambdaQueryWrapper<>();
        deptWrapper.eq(SysDepartment::getCode, parentCode);
        SysDepartment department = departmentMapper.selectOne(deptWrapper);

        Long departmentId;
        Long parentId = null;

        if (department != null) {
            departmentId = department.getId();
        } else {
            // 查找上级岗位
            LambdaQueryWrapper<SysPosition> posWrapper = new LambdaQueryWrapper<>();
            posWrapper.eq(SysPosition::getCode, parentCode);
            SysPosition parentPos = positionMapper.selectOne(posWrapper);
            if (parentPos == null) {
                throw new RuntimeException("上级岗位或部门不存在");
            }
            departmentId = parentPos.getDepartmentId();
            parentId = parentPos.getId();
        }

        SysPosition position = SysPosition.builder()
                .parentId(parentId)
                .departmentId(departmentId)
                .code(code)
                .name(name)
                .status(status)
                .sort(sort)
                .remark(remark)
                .build();
        positionMapper.insert(position);
    }

    /**
     * 导入用户
     *
     * @param parentCode 上级编码
     * @param code       编码
     * @param name       名称
     * @param status     状态
     * @param sort       排序号
     * @param remark     备注
     */
    private void importUser(String parentCode, String code, String name, int status, int sort, String remark) {
        // 检查编码是否存在
        LambdaQueryWrapper<SysOrgUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysOrgUser::getCode, code);
        int count = orgUserMapper.selectCount(wrapper);
        if (count > 0) {
            throw new RuntimeException("用户编码已存在");
        }

        if (StrUtil.isBlank(parentCode)) {
            throw new RuntimeException("用户必须指定所属岗位编码");
        }

        LambdaQueryWrapper<SysPosition> posWrapper = new LambdaQueryWrapper<>();
        posWrapper.eq(SysPosition::getCode, parentCode);
        SysPosition position = positionMapper.selectOne(posWrapper);
        if (position == null) {
            throw new RuntimeException("所属岗位不存在");
        }

        SysOrgUser user = SysOrgUser.builder()
                .positionId(position.getId())
                .code(code)
                .name(name)
                .status(status)
                .sort(sort)
                .remark(remark)
                .build();
        orgUserMapper.insert(user);
    }
}

