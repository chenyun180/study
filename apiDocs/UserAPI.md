# 用户模块 API 文档

## 获取用户信息

### 接口名称
获取当前登录用户的基本信息

### 接口地址
`/api/user/info`

### 请求方式
POST

### 功能说明
通过 JWT Token 获取当前登录用户的详细信息，包括用户基本信息、角色列表和权限列表。

### 请求参数

无请求体参数，通过 Authorization header 传递 Token：

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 响应参数

参数含义：

| 参数名 | 类型 | 是否必填 | 字段说明 | 示例值 |
|---|---|:--|:--|:--|
| code | int | 是 | 响应码（0表示成功） | 0 |
| msg | String | 是 | 响应消息 | 获取用户信息成功 |
| data | Object | 否 | 用户信息对象 | {} |
| data.id | Long | 是 | 用户ID | 1 |
| data.name | String | 是 | 用户名称 | 系统管理员 |
| data.email | String | 是 | 用户邮箱 | admin@example.com |
| data.avatar | String | 否 | 用户头像URL | https://avatars.githubusercontent.com/u/1?v=4 |
| data.roles | List\<String\> | 是 | 用户角色列表 | ["admin", "user"] |
| data.permissions | List\<String\> | 否 | 用户权限列表 | ["*:*:*"] |

参数示例json：

**成功响应：**
```json
{
    "code": 0,
    "msg": "获取用户信息成功",
    "data": {
        "id": 1,
        "name": "系统管理员",
        "email": "admin@example.com",
        "avatar": "https://avatars.githubusercontent.com/u/1?v=4",
        "roles": ["admin", "user"],
        "permissions": ["*:*:*"]
    }
}
```

**失败响应：**
```json
{
    "code": -1,
    "msg": "未登录或登录已过期",
    "data": null
}
```

### 错误码说明

| code | 说明 |
|------|------|
| 0 | 请求成功 |
| -1 | 请求失败（Token无效、用户不存在等） |
| 401 | 未授权（未登录或Token已过期） |

