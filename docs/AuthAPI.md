# 认证 API 文档

本文档描述了基于 SpringSecurity + JWT 的认证相关 API 接口。

## 目录

- [用户登录](#用户登录)
- [获取用户信息](#获取用户信息)
- [用户登出](#用户登出)
- [刷新 Token](#刷新-token)

---

## 用户登录

### 基本信息

**接口名称：** 用户登录

**功能描述：** 用户通过用户名和密码登录系统，验证成功后返回 JWT access token 和 refresh token

**接口地址：** `/api/auth/login`

**请求方式：** POST

### 功能说明

1. 接收用户名和密码
2. 验证用户凭证
3. 生成 JWT access token（有效期 2 小时）
4. 生成 JWT refresh token（有效期 7 天）
5. 返回 token 信息

### 请求参数

```json
{
  "username": "admin",
  "password": "123456",
  "rememberMe": true
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| username | string | 是 | 用户名 | admin |
| password | string | 是 | 密码 | 123456 |
| rememberMe | boolean | 否 | 是否记住密码（影响 token 有效期） | true |

### 响应参数

```json
{
  "code": 0,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 7200,
    "tokenType": "Bearer"
  },
  "msg": "登录成功",
  "timestamp": 1703001234567
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| code | int | 是 | 响应码（0 表示成功） | 0 |
| msg | string | 是 | 响应消息 | 登录成功 |
| timestamp | long | 是 | 时间戳 | 1703001234567 |
| data | object | 是 | 响应数据 | |
| data.token | string | 是 | JWT access token | eyJhbGci... |
| data.refreshToken | string | 是 | JWT refresh token | eyJhbGci... |
| data.expiresIn | int | 是 | token 过期时间（秒） | 7200 |
| data.tokenType | string | 是 | token 类型 | Bearer |

### 错误响应

```json
{
  "code": 401,
  "data": null,
  "msg": "用户名或密码错误",
  "timestamp": 1703001234567
}
```

### 测试账号

| 用户名 | 密码 | 角色 | 说明 |
|--------|------|------|------|
| admin | 123456 | admin, user | 管理员账号，拥有所有权限 |
| user | 123456 | user | 普通用户账号，部分权限 |

### 使用示例

```typescript
import { login } from '@/api/modules/user'

const response = await login({
  username: 'admin',
  password: '123456',
  rememberMe: true
})

// 存储 token
localStorage.setItem('token', response.data.token)
```

---

## 获取用户信息

### 基本信息

**接口名称：** 获取当前登录用户信息

**功能描述：** 通过 JWT token 获取当前登录用户的详细信息，包括用户基本信息、角色和权限

**接口地址：** `/api/user/info`

**请求方式：** POST

### 功能说明

1. 从请求头中获取 JWT token
2. 验证 token 有效性
3. 解析 token 获取用户 ID
4. 查询并返回用户详细信息

### 请求参数

无请求体参数，通过 Authorization header 传递 token：

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 响应参数

```json
{
  "code": 0,
  "data": {
    "id": 1,
    "name": "系统管理员",
    "email": "admin@example.com",
    "avatar": "https://avatars.githubusercontent.com/u/1?v=4",
    "roles": ["admin", "user"],
    "permissions": ["*:*:*"]
  },
  "msg": "获取用户信息成功",
  "timestamp": 1703001234567
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| code | int | 是 | 响应码 | 0 |
| msg | string | 是 | 响应消息 | 获取用户信息成功 |
| data | object | 是 | 用户信息 | |
| data.id | int | 是 | 用户 ID | 1 |
| data.name | string | 是 | 用户名称 | 系统管理员 |
| data.email | string | 是 | 用户邮箱 | admin@example.com |
| data.avatar | string | 否 | 用户头像 URL | https://... |
| data.roles | array | 是 | 用户角色列表 | ["admin", "user"] |
| data.permissions | array | 否 | 用户权限列表 | ["*:*:*"] |

### 错误响应

```json
{
  "code": 401,
  "data": null,
  "msg": "未登录或登录已过期",
  "timestamp": 1703001234567
}
```

### 使用示例

```typescript
import { getUserInfo } from '@/api/modules/user'

const response = await getUserInfo()
console.log('用户信息:', response.data)
```

---

## 用户登出

### 基本信息

**接口名称：** 用户登出

**功能描述：** 用户退出登录，清除服务端 token 记录（可选），客户端需清除本地存储的 token

**接口地址：** `/api/auth/logout`

**请求方式：** POST

### 功能说明

1. 从请求头获取 token
2. 清除服务端 token 记录（如使用 Redis 存储）
3. 返回登出成功响应

### 请求参数

无请求体参数，通过 Authorization header 传递 token

### 响应参数

```json
{
  "code": 0,
  "data": null,
  "msg": "登出成功",
  "timestamp": 1703001234567
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| code | int | 是 | 响应码 | 0 |
| msg | string | 是 | 响应消息 | 登出成功 |
| data | null | 是 | 无数据 | null |

### 使用示例

```typescript
import { logout } from '@/api/modules/user'

await logout()

// 清除本地 token
localStorage.removeItem('token')
localStorage.removeItem('refreshToken')

// 跳转到登录页
router.push('/login')
```

---

## 刷新 Token

### 基本信息

**接口名称：** 刷新 Token

**功能描述：** 使用 refresh token 获取新的 access token，避免用户频繁登录

**接口地址：** `/api/auth/refresh`

**请求方式：** POST

### 功能说明

1. 接收 refresh token
2. 验证 refresh token 有效性
3. 生成新的 access token
4. 返回新 token

### 请求参数

```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| refreshToken | string | 是 | JWT refresh token | eyJhbGci... |

### 响应参数

```json
{
  "code": 0,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 7200
  },
  "msg": "token 刷新成功",
  "timestamp": 1703001234567
}
```

| 参数名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| code | int | 是 | 响应码 | 0 |
| msg | string | 是 | 响应消息 | token 刷新成功 |
| data | object | 是 | 响应数据 | |
| data.token | string | 是 | 新的 access token | eyJhbGci... |
| data.expiresIn | int | 是 | 过期时间（秒） | 7200 |

### 错误响应

```json
{
  "code": 401,
  "data": null,
  "msg": "refresh token 无效",
  "timestamp": 1703001234567
}
```

### 使用示例

```typescript
import { refreshToken } from '@/api/modules/user'

const oldRefreshToken = localStorage.getItem('refreshToken')
const response = await refreshToken(oldRefreshToken)

// 更新 token
localStorage.setItem('token', response.data.token)
```

---

## 响应状态码说明

| 状态码 | 说明 |
|--------|------|
| 0 | 请求成功 |
| -1 | 通用错误 |
| 401 | 未授权（未登录或 token 无效） |
| 403 | 禁止访问（无权限） |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

## JWT Token 说明

### Token 格式

```
Authorization: Bearer <token>
```

### Token 结构

JWT Token 由三部分组成，使用 `.` 分隔：

```
Header.Payload.Signature
```

#### Header（头部）
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

#### Payload（载荷）
```json
{
  "sub": "1",
  "username": "admin",
  "roles": ["admin", "user"],
  "iat": 1703001234,
  "exp": 1703008434
}
```

#### Signature（签名）
使用密钥对 Header 和 Payload 进行签名

### Token 有效期

- **Access Token**: 2 小时（rememberMe 为 false）或 7 天（rememberMe 为 true）
- **Refresh Token**: 7 天

### Token 刷新策略

1. 前端在请求拦截器中检查 token 是否即将过期
2. 如果 token 在 5 分钟内过期，自动使用 refresh token 刷新
3. 如果 refresh token 也过期，跳转到登录页

## 安全建议

1. **HTTPS**: 生产环境必须使用 HTTPS 传输
2. **密钥管理**: JWT 签名密钥应使用环境变量配置，不要硬编码
3. **Token 存储**: 
   - 推荐使用 httpOnly Cookie 存储（防止 XSS）
   - 如使用 localStorage，注意 XSS 防护
4. **刷新策略**: 实现 token 自动刷新机制
5. **登出处理**: 服务端应维护 token 黑名单（使用 Redis）
6. **密码加密**: 前端传输前应对密码进行加密（如 RSA）
7. **防暴力破解**: 实现登录失败次数限制和验证码机制

## 前端集成说明

### 1. 请求拦截器

```typescript
// 在请求拦截器中添加 token
service.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})
```

### 2. 响应拦截器

```typescript
// 在响应拦截器中处理 401 错误
service.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response?.status === 401) {
      // Token 过期，跳转登录
      localStorage.removeItem('token')
      router.push('/login')
    }
    return Promise.reject(error)
  }
)
```

### 3. 路由守卫

```typescript
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    // 需要登录但未登录，跳转到登录页
    next('/login')
  } else {
    next()
  }
})
```

## Mock 数据说明

开发环境已配置 Mock 数据，无需连接真实后端即可测试：

1. 设置环境变量 `VITE_USE_MOCK=true`
2. Mock 数据位于 `src/api/mock/auth.mock.ts`
3. 支持所有认证相关 API
4. 模拟真实的 token 验证和过期机制

## 常见问题

### Q1: Token 过期后如何处理？

A: 使用 refresh token 自动刷新，或提示用户重新登录。

### Q2: 如何实现"记住我"功能？

A: 通过 rememberMe 参数控制 token 有效期，true 时为 7 天，false 时为 2 小时。

### Q3: 多设备登录如何处理？

A: 服务端可以限制同一用户的 token 数量，或实现设备管理功能。

### Q4: 如何防止 token 被盗用？

A: 
- 使用 HTTPS
- 绑定 IP 地址或设备指纹
- 实现异常登录检测
- 定期强制更新密码

## 更新日志

| 版本 | 日期 | 说明 |
|------|------|------|
| 1.0.0 | 2024-12-29 | 初始版本，实现基础认证功能 |

