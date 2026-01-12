# 组织机构管理 API 文档

## 概述

本文档描述组织机构管理模块的所有 API 接口，包括公司、部门、岗位、用户的增删改查操作。

---

## 1. 获取组织机构树

**接口名称：** 获取组织机构树  
**功能描述：** 获取组织机构的树形结构数据，用于左侧树形组件展示  
**接口地址：** /api/organization/tree  
**请求方式：** POST

### 请求参数

```json
{
  "keyword": "研发"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| keyword | string | 否 | 搜索关键词，用于过滤节点名称 | 研发 |

### 响应参数

```json
{
  "code": 0,
  "data": [
    {
      "id": 1,
      "parentId": null,
      "code": "RQ",
      "name": "润祺智慧",
      "type": "company",
      "path": "润祺智慧",
      "status": "active",
      "sort": 1,
      "children": [
        {
          "id": 2,
          "parentId": 1,
          "code": "RQ-SH",
          "name": "上海分公司",
          "type": "company",
          "path": "润祺智慧/上海分公司",
          "status": "active",
          "sort": 1,
          "children": []
        }
      ]
    }
  ],
  "msg": "获取组织机构树成功"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| code | int | 是 | 错误码，0表示成功 | 0 |
| msg | string | 是 | 响应消息 | 获取组织机构树成功 |
| data | array | 是 | 树形结构数据 | |
| data[].id | int | 是 | 节点ID | 1 |
| data[].parentId | int | 否 | 父节点ID，根节点为null | null |
| data[].code | string | 是 | 编码 | RQ |
| data[].name | string | 是 | 名称 | 润祺智慧 |
| data[].type | string | 是 | 类型：company/department/position/user | company |
| data[].path | string | 是 | 完整路径 | 润祺智慧 |
| data[].status | string | 是 | 状态：active/inactive | active |
| data[].sort | int | 是 | 排序号 | 1 |
| data[].children | array | 否 | 子节点列表 | [] |

---

## 2. 获取组织机构列表

**接口名称：** 获取组织机构列表  
**功能描述：** 根据父节点ID和类型筛选获取组织机构列表，支持分页  
**接口地址：** /api/organization/list  
**请求方式：** POST

### 请求参数

```json
{
  "parentId": 1,
  "type": "department",
  "code": "RD",
  "name": "研发",
  "current": 1,
  "size": 10
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| parentId | int | 否 | 父节点ID | 1 |
| type | string | 否 | 类型筛选：company/department/position/user | department |
| code | string | 否 | 编码模糊搜索 | RD |
| name | string | 否 | 名称模糊搜索 | 研发 |
| current | int | 否 | 当前页码，默认1 | 1 |
| size | int | 否 | 每页数量，默认10 | 10 |

### 响应参数

```json
{
  "code": 0,
  "data": {
    "records": [
      {
        "id": 101,
        "type": "department",
        "code": "SH-RD",
        "name": "研发部",
        "path": "润祺智慧/上海分公司/研发部",
        "status": "active",
        "createTime": "2020-06-01 00:00:00"
      }
    ],
    "total": 50,
    "current": 1,
    "size": 10,
    "pages": 5
  },
  "msg": "获取组织机构列表成功"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| code | int | 是 | 错误码 | 0 |
| msg | string | 是 | 响应消息 | 获取组织机构列表成功 |
| data | object | 是 | 分页数据 | |
| data.records | array | 是 | 数据列表 | |
| data.records[].id | int | 是 | ID | 101 |
| data.records[].type | string | 是 | 类型 | department |
| data.records[].code | string | 是 | 编码 | SH-RD |
| data.records[].name | string | 是 | 名称 | 研发部 |
| data.records[].path | string | 是 | 完整路径 | 润祺智慧/上海分公司/研发部 |
| data.records[].status | string | 是 | 状态 | active |
| data.records[].createTime | string | 否 | 创建时间 | 2020-06-01 00:00:00 |
| data.total | int | 是 | 总记录数 | 50 |
| data.current | int | 是 | 当前页码 | 1 |
| data.size | int | 是 | 每页数量 | 10 |
| data.pages | int | 是 | 总页数 | 5 |

---

## 3. 获取公司详情

**接口名称：** 获取公司详情  
**功能描述：** 根据ID获取公司的详细信息  
**接口地址：** /api/organization/company/detail  
**请求方式：** POST

### 请求参数

```json
{
  "id": 1
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| id | int | 是 | 公司ID | 1 |

### 响应参数

```json
{
  "code": 0,
  "data": {
    "id": 1,
    "parentId": null,
    "code": "RQ",
    "name": "润祺智慧",
    "shortName": "润祺",
    "address": "深圳市南山区科技园",
    "phone": "0755-12345678",
    "email": "contact@runqi.com",
    "legalPerson": "张总",
    "status": "active",
    "sort": 1,
    "remark": "集团总部",
    "createTime": "2020-01-01 00:00:00",
    "updateTime": "2024-01-01 00:00:00"
  },
  "msg": "获取公司详情成功"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| code | int | 是 | 错误码 | 0 |
| msg | string | 是 | 响应消息 | 获取公司详情成功 |
| data | object | 是 | 公司信息 | |
| data.id | int | 是 | 公司ID | 1 |
| data.parentId | int | 否 | 上级公司ID | null |
| data.code | string | 是 | 编码 | RQ |
| data.name | string | 是 | 名称 | 润祺智慧 |
| data.shortName | string | 否 | 简称 | 润祺 |
| data.address | string | 否 | 地址 | 深圳市南山区科技园 |
| data.phone | string | 否 | 电话 | 0755-12345678 |
| data.email | string | 否 | 邮箱 | contact@runqi.com |
| data.legalPerson | string | 否 | 法人 | 张总 |
| data.status | string | 是 | 状态 | active |
| data.sort | int | 是 | 排序号 | 1 |
| data.remark | string | 否 | 备注 | 集团总部 |
| data.createTime | string | 否 | 创建时间 | 2020-01-01 00:00:00 |
| data.updateTime | string | 否 | 更新时间 | 2024-01-01 00:00:00 |

---

## 4. 新增/编辑公司

**接口名称：** 新增/编辑公司  
**功能描述：** 新增或编辑公司信息，有id为编辑，无id为新增  
**接口地址：** /api/organization/company/save  
**请求方式：** POST

### 请求参数

```json
{
  "id": 1,
  "parentId": null,
  "code": "RQ",
  "name": "润祺智慧",
  "shortName": "润祺",
  "address": "深圳市南山区科技园",
  "phone": "0755-12345678",
  "email": "contact@runqi.com",
  "legalPerson": "张总",
  "status": "active",
  "sort": 1,
  "remark": "集团总部"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| id | int | 否 | 公司ID（编辑时必填） | 1 |
| parentId | int | 否 | 上级公司ID | null |
| code | string | 是 | 编码 | RQ |
| name | string | 是 | 名称 | 润祺智慧 |
| shortName | string | 否 | 简称 | 润祺 |
| address | string | 否 | 地址 | 深圳市南山区科技园 |
| phone | string | 否 | 电话 | 0755-12345678 |
| email | string | 否 | 邮箱 | contact@runqi.com |
| legalPerson | string | 否 | 法人 | 张总 |
| status | string | 否 | 状态，默认active | active |
| sort | int | 否 | 排序号，默认1 | 1 |
| remark | string | 否 | 备注 | 集团总部 |

### 响应参数

```json
{
  "code": 0,
  "data": {
    "id": 1
  },
  "msg": "保存成功"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| code | int | 是 | 错误码 | 0 |
| msg | string | 是 | 响应消息 | 保存成功 |
| data | object | 否 | 返回数据（新增时返回ID） | |
| data.id | int | 否 | 新增的公司ID | 1 |

---

## 5. 获取部门详情

**接口名称：** 获取部门详情  
**功能描述：** 根据ID获取部门的详细信息  
**接口地址：** /api/organization/department/detail  
**请求方式：** POST

### 请求参数

```json
{
  "id": 101
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| id | int | 是 | 部门ID | 101 |

### 响应参数

```json
{
  "code": 0,
  "data": {
    "id": 101,
    "parentId": null,
    "companyId": 2,
    "code": "SH-RD",
    "name": "研发部",
    "leaderId": 1001,
    "leaderName": "张研发",
    "phone": "021-11111111",
    "email": "rd@shanghai.runqi.com",
    "status": "active",
    "sort": 1,
    "remark": null,
    "createTime": "2020-06-01 00:00:00",
    "updateTime": "2024-01-01 00:00:00"
  },
  "msg": "获取部门详情成功"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| code | int | 是 | 错误码 | 0 |
| msg | string | 是 | 响应消息 | 获取部门详情成功 |
| data | object | 是 | 部门信息 | |
| data.id | int | 是 | 部门ID | 101 |
| data.parentId | int | 否 | 上级部门ID | null |
| data.companyId | int | 是 | 所属公司ID | 2 |
| data.code | string | 是 | 编码 | SH-RD |
| data.name | string | 是 | 名称 | 研发部 |
| data.leaderId | int | 否 | 负责人ID | 1001 |
| data.leaderName | string | 否 | 负责人姓名 | 张研发 |
| data.phone | string | 否 | 电话 | 021-11111111 |
| data.email | string | 否 | 邮箱 | rd@shanghai.runqi.com |
| data.status | string | 是 | 状态 | active |
| data.sort | int | 是 | 排序号 | 1 |
| data.remark | string | 否 | 备注 | null |
| data.createTime | string | 否 | 创建时间 | 2020-06-01 00:00:00 |
| data.updateTime | string | 否 | 更新时间 | 2024-01-01 00:00:00 |

---

## 6. 新增/编辑部门

**接口名称：** 新增/编辑部门  
**功能描述：** 新增或编辑部门信息，有id为编辑，无id为新增  
**接口地址：** /api/organization/department/save  
**请求方式：** POST

### 请求参数

```json
{
  "id": 101,
  "parentId": null,
  "companyId": 2,
  "code": "SH-RD",
  "name": "研发部",
  "leaderId": 1001,
  "phone": "021-11111111",
  "email": "rd@shanghai.runqi.com",
  "status": "active",
  "sort": 1,
  "remark": null
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| id | int | 否 | 部门ID（编辑时必填） | 101 |
| parentId | int | 否 | 上级部门ID | null |
| companyId | int | 是 | 所属公司ID | 2 |
| code | string | 是 | 编码 | SH-RD |
| name | string | 是 | 名称 | 研发部 |
| leaderId | int | 否 | 负责人ID | 1001 |
| phone | string | 否 | 电话 | 021-11111111 |
| email | string | 否 | 邮箱 | rd@shanghai.runqi.com |
| status | string | 否 | 状态，默认active | active |
| sort | int | 否 | 排序号，默认1 | 1 |
| remark | string | 否 | 备注 | null |

### 响应参数

```json
{
  "code": 0,
  "data": {
    "id": 101
  },
  "msg": "保存成功"
}
```

---

## 7. 获取岗位详情

**接口名称：** 获取岗位详情  
**功能描述：** 根据ID获取岗位的详细信息  
**接口地址：** /api/organization/position/detail  
**请求方式：** POST

### 请求参数

```json
{
  "id": 201
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| id | int | 是 | 岗位ID | 201 |

### 响应参数

```json
{
  "code": 0,
  "data": {
    "id": 201,
    "parentId": null,
    "departmentId": 101,
    "code": "SH-RD-TEST",
    "name": "测试岗位",
    "level": 3,
    "status": "active",
    "sort": 1,
    "remark": null,
    "createTime": "2020-06-01 00:00:00",
    "updateTime": "2024-01-01 00:00:00"
  },
  "msg": "获取岗位详情成功"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| code | int | 是 | 错误码 | 0 |
| msg | string | 是 | 响应消息 | 获取岗位详情成功 |
| data | object | 是 | 岗位信息 | |
| data.id | int | 是 | 岗位ID | 201 |
| data.parentId | int | 否 | 上级岗位ID | null |
| data.departmentId | int | 是 | 所属部门ID | 101 |
| data.code | string | 是 | 编码 | SH-RD-TEST |
| data.name | string | 是 | 名称 | 测试岗位 |
| data.level | int | 否 | 岗位级别 | 3 |
| data.status | string | 是 | 状态 | active |
| data.sort | int | 是 | 排序号 | 1 |
| data.remark | string | 否 | 备注 | null |
| data.createTime | string | 否 | 创建时间 | 2020-06-01 00:00:00 |
| data.updateTime | string | 否 | 更新时间 | 2024-01-01 00:00:00 |

---

## 8. 新增/编辑岗位

**接口名称：** 新增/编辑岗位  
**功能描述：** 新增或编辑岗位信息，有id为编辑，无id为新增  
**接口地址：** /api/organization/position/save  
**请求方式：** POST

### 请求参数

```json
{
  "id": 201,
  "parentId": null,
  "departmentId": 101,
  "code": "SH-RD-TEST",
  "name": "测试岗位",
  "level": 3,
  "status": "active",
  "sort": 1,
  "remark": null
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| id | int | 否 | 岗位ID（编辑时必填） | 201 |
| parentId | int | 否 | 上级岗位ID | null |
| departmentId | int | 是 | 所属部门ID | 101 |
| code | string | 是 | 编码 | SH-RD-TEST |
| name | string | 是 | 名称 | 测试岗位 |
| level | int | 否 | 岗位级别（1-10） | 3 |
| status | string | 否 | 状态，默认active | active |
| sort | int | 否 | 排序号，默认1 | 1 |
| remark | string | 否 | 备注 | null |

### 响应参数

```json
{
  "code": 0,
  "data": {
    "id": 201
  },
  "msg": "保存成功"
}
```

---

## 9. 获取组织用户详情

**接口名称：** 获取组织用户详情  
**功能描述：** 根据ID获取组织用户的详细信息  
**接口地址：** /api/organization/user/detail  
**请求方式：** POST

### 请求参数

```json
{
  "id": 1001
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| id | int | 是 | 用户ID | 1001 |

### 响应参数

```json
{
  "code": 0,
  "data": {
    "id": 1001,
    "positionId": 201,
    "code": "U001",
    "name": "张三",
    "phone": "13800138000",
    "email": "zhangsan@runqi.com",
    "status": "active",
    "sort": 1,
    "remark": null,
    "createTime": "2020-06-01 00:00:00",
    "updateTime": "2024-01-01 00:00:00"
  },
  "msg": "获取用户详情成功"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| code | int | 是 | 错误码 | 0 |
| msg | string | 是 | 响应消息 | 获取用户详情成功 |
| data | object | 是 | 用户信息 | |
| data.id | int | 是 | 用户ID | 1001 |
| data.positionId | int | 是 | 所属岗位ID | 201 |
| data.code | string | 是 | 编码 | U001 |
| data.name | string | 是 | 名称 | 张三 |
| data.phone | string | 否 | 电话 | 13800138000 |
| data.email | string | 否 | 邮箱 | zhangsan@runqi.com |
| data.status | string | 是 | 状态 | active |
| data.sort | int | 是 | 排序号 | 1 |
| data.remark | string | 否 | 备注 | null |
| data.createTime | string | 否 | 创建时间 | 2020-06-01 00:00:00 |
| data.updateTime | string | 否 | 更新时间 | 2024-01-01 00:00:00 |

---

## 10. 新增/编辑组织用户

**接口名称：** 新增/编辑组织用户  
**功能描述：** 新增或编辑组织用户信息，有id为编辑，无id为新增  
**接口地址：** /api/organization/user/save  
**请求方式：** POST

### 请求参数

```json
{
  "id": 1001,
  "positionId": 201,
  "code": "U001",
  "name": "张三",
  "phone": "13800138000",
  "email": "zhangsan@runqi.com",
  "status": "active",
  "sort": 1,
  "remark": null
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| id | int | 否 | 用户ID（编辑时必填） | 1001 |
| positionId | int | 是 | 所属岗位ID | 201 |
| code | string | 是 | 编码 | U001 |
| name | string | 是 | 名称 | 张三 |
| phone | string | 否 | 电话 | 13800138000 |
| email | string | 否 | 邮箱 | zhangsan@runqi.com |
| status | string | 否 | 状态，默认active | active |
| sort | int | 否 | 排序号，默认1 | 1 |
| remark | string | 否 | 备注 | null |

### 响应参数

```json
{
  "code": 0,
  "data": {
    "id": 1001
  },
  "msg": "保存成功"
}
```

---

## 11. 删除组织机构节点

**接口名称：** 删除组织机构节点  
**功能描述：** 根据ID和类型删除组织机构节点（公司/部门/岗位/用户）  
**接口地址：** /api/organization/delete  
**请求方式：** POST

### 请求参数

```json
{
  "id": 1,
  "type": "company"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| id | int | 是 | 节点ID | 1 |
| type | string | 是 | 节点类型：company/department/position/user | company |

### 响应参数

```json
{
  "code": 0,
  "data": null,
  "msg": "删除成功"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| code | int | 是 | 错误码 | 0 |
| msg | string | 是 | 响应消息 | 删除成功 |
| data | null | 是 | 返回数据 | null |

### 错误响应

```json
{
  "code": 400,
  "data": null,
  "msg": "该公司下存在子节点，无法删除"
}
```

---

## 12. 下载导入模板

**接口名称：** 下载导入模板  
**功能描述：** 下载组织机构导入Excel模板  
**接口地址：** /api/organization/template/download  
**请求方式：** POST

### 请求参数

无

### 响应参数

返回 Excel 文件流（application/vnd.openxmlformats-officedocument.spreadsheetml.sheet）

---

## 13. 导入组织机构数据

**接口名称：** 导入组织机构数据  
**功能描述：** 通过Excel文件批量导入组织机构数据  
**接口地址：** /api/organization/import  
**请求方式：** POST

### 请求参数

Content-Type: multipart/form-data

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| file | File | 是 | Excel文件 | organization.xlsx |

### 响应参数

```json
{
  "code": 0,
  "data": {
    "success": 10,
    "fail": 2,
    "errors": [
      "第3行：编码已存在",
      "第7行：上级部门不存在"
    ]
  },
  "msg": "导入完成"
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| code | int | 是 | 错误码 | 0 |
| msg | string | 是 | 响应消息 | 导入完成 |
| data | object | 是 | 导入结果 | |
| data.success | int | 是 | 成功条数 | 10 |
| data.fail | int | 是 | 失败条数 | 2 |
| data.errors | array | 是 | 错误详情 | ["第3行：编码已存在"] |

---

## 错误码说明

| 错误码 | 说明 |
|--------|------|
| 0 | 成功 |
| 400 | 请求参数错误 |
| 401 | 未授权 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

