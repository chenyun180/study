# 单点登录（SSO）功能文档

本文档详细描述了基于 Spring Security + OAuth2 + JWT 的单点登录功能实现。

## 目录

- [系统架构](#系统架构)
- [模块说明](#模块说明)
- [API接口](#api接口)
  - [用户登录](#用户登录)
  - [获取用户信息](#获取用户信息)
  - [用户登出](#用户登出)
  - [刷新Token](#刷新token)
- [JWT Token说明](#jwt-token说明)
- [网关认证流程](#网关认证流程)
- [配置说明](#配置说明)
- [快速开始](#快速开始)
- [安全建议](#安全建议)
- [常见问题](#常见问题)

---

## 系统架构

```
┌─────────────────────────────────────────────────────────────────────┐
│                           前端应用                                   │
└─────────────────────────────────┬───────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────────┐
│                    cloud-gateway-client (网关)                       │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │  JwtAuthenticationFilter (JWT认证过滤器)                        │ │
│  │  - 白名单路径直接放行                                            │ │
│  │  - 验证Token有效性                                               │ │
│  │  - 检查Token黑名单                                               │ │
│  │  - 传递用户信息到下游服务                                         │ │
│  └────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────┬───────────────────────────────────┘
                                  │
          ┌───────────────────────┼───────────────────────┐
          ▼                       ▼                       ▼
┌──────────────────┐   ┌──────────────────┐   ┌──────────────────┐
│   cloud-auth     │   │   cloud-admin    │   │   cloud-test     │
│   (认证服务)      │   │   (业务服务)      │   │   (测试服务)      │
│                  │   │                  │   │                  │
│ - 用户登录        │   │                  │   │                  │
│ - Token刷新      │   │                  │   │                  │
│ - 用户登出        │   │                  │   │                  │
│ - 获取用户信息    │   │                  │   │                  │
└────────┬─────────┘   └──────────────────┘   └──────────────────┘
         │
         ▼
┌──────────────────┐
│      Redis       │
│  (Token缓存)     │
│  - Access Token  │
│  - Refresh Token │
│  - Token黑名单   │
└──────────────────┘
```

---

## 模块说明

### cloud-auth（认证服务）

负责用户认证相关的功能（登录/登出/刷新Token）：

| 包路径 | 说明 |
|--------|------|
| `com.cloud.auth.controller` | 认证相关控制器 |
| `com.cloud.auth.service` | 认证服务层 |
| `com.cloud.auth.security` | Spring Security配置和过滤器 |
| `com.cloud.auth.config` | 配置类 |
| `com.cloud.auth.model.dto` | 数据传输对象 |
| `com.cloud.auth.model.entity` | 实体类 |

#### 核心类说明

| 类名 | 说明 |
|------|------|
| `AuthController` | 认证控制器，提供登录/登出/刷新Token接口 |
| `AuthService` | 认证服务，处理认证业务逻辑 |
| `SecurityConfig` | Spring Security配置类 |
| `JwtAuthenticationFilter` | JWT认证过滤器 |
| `JwtAuthenticationEntryPoint` | 认证入口点，处理未认证请求 |
| `LoginUser` | Spring Security用户详情实现 |
| `UserDetailsServiceImpl` | 用户详情服务实现 |

### cloud-service-admin（用户管理服务）

负责用户信息相关的功能：

| 包路径 | 说明 |
|--------|------|
| `com.cloud.admin.controller` | 用户相关控制器 |
| `com.cloud.admin.service` | 用户服务层 |
| `com.cloud.admin.model.dto` | 数据传输对象 |
| `com.cloud.admin.model.entity` | 实体类 |

#### 核心类说明

| 类名 | 说明 |
|------|------|
| `UserController` | 用户控制器，提供获取用户信息接口 |
| `SysUserService` | 用户服务，处理用户信息查询 |

### cloud-gateway-client（网关服务）

负责请求路由和统一认证：

| 类名 | 说明 |
|------|------|
| `JwtAuthenticationFilter` | 网关JWT认证过滤器 |
| `GatewayConfig` | 网关配置类（跨域等） |

### cloud-service-common（公共模块）

提供通用工具和常量：

| 类名 | 说明 |
|------|------|
| `JwtUtil` | JWT工具类 |
| `AuthConstants` | 认证相关常量 |
| `RedisConstants` | Redis Key常量 |

---

## API接口

### 用户登录

#### 基本信息

| 项目 | 说明 |
|------|------|
| **接口名称** | 用户登录 |
| **接口地址** | `/api/auth/login` |
| **请求方式** | POST |
| **功能描述** | 用户通过用户名和密码登录系统，验证成功后返回JWT Token |

#### 请求参数

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
| rememberMe | boolean | 否 | 是否记住我（影响Token有效期） | true |

#### 响应参数

**成功响应：**

```json
{
  "code": 0,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 7200,
    "tokenType": "Bearer"
  },
  "msg": "登录成功"
}
```

| 参数名 | 类型 | 说明 |
|--------|------|------|
| code | int | 响应码（0表示成功） |
| msg | string | 响应消息 |
| data.token | string | JWT Access Token |
| data.refreshToken | string | JWT Refresh Token |
| data.expiresIn | long | Token过期时间（秒） |
| data.tokenType | string | Token类型（Bearer） |

**失败响应：**

```json
{
  "code": -1,
  "data": null,
  "msg": "用户名或密码错误"
}
```

#### 测试账号

| 用户名 | 密码 | 角色 | 权限 |
|--------|------|------|------|
| admin | 123456 | admin, user | *:*:* (所有权限) |
| user | 123456 | user | system:user:view, system:user:edit |

---

### 获取用户信息

#### 基本信息

| 项目 | 说明 |
|------|------|
| **接口名称** | 获取当前登录用户信息 |
| **接口地址** | `/api/user/info` |
| **请求方式** | POST |
| **功能描述** | 通过JWT Token获取当前登录用户的详细信息 |

#### 请求参数

无请求体参数，通过 Authorization header 传递 Token：

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

#### 响应参数

**成功响应：**

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
  "msg": "获取用户信息成功"
}
```

| 参数名 | 类型 | 说明 |
|--------|------|------|
| data.id | long | 用户ID |
| data.name | string | 用户名称 |
| data.email | string | 用户邮箱 |
| data.avatar | string | 用户头像URL |
| data.roles | array | 用户角色列表 |
| data.permissions | array | 用户权限列表 |

---

### 用户登出

#### 基本信息

| 项目 | 说明 |
|------|------|
| **接口名称** | 用户登出 |
| **接口地址** | `/api/auth/logout` |
| **请求方式** | POST |
| **功能描述** | 用户退出登录，清除服务端Token记录，将Token加入黑名单 |

#### 请求参数

无请求体参数，通过 Authorization header 传递 Token。

#### 响应参数

```json
{
  "code": 0,
  "data": null,
  "msg": "登出成功"
}
```

---

### 刷新Token

#### 基本信息

| 项目 | 说明 |
|------|------|
| **接口名称** | 刷新Token |
| **接口地址** | `/api/auth/refresh` |
| **请求方式** | POST |
| **功能描述** | 使用Refresh Token获取新的Access Token |

#### 请求参数

```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| refreshToken | string | 是 | Refresh Token |

#### 响应参数

**成功响应：**

```json
{
  "code": 0,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "expiresIn": 7200
  },
  "msg": "Token刷新成功"
}
```

| 参数名 | 类型 | 说明 |
|--------|------|------|
| data.token | string | 新的Access Token |
| data.expiresIn | long | 过期时间（秒） |

---

## JWT Token说明

### Token结构

JWT Token由三部分组成，使用 `.` 分隔：

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
  "iss": "cloud-auth",
  "iat": 1703001234,
  "exp": 1703008434,
  "userId": 1,
  "username": "admin",
  "roles": ["admin", "user"],
  "tokenType": "access"
}
```

| 字段 | 说明 |
|------|------|
| sub | 主题（用户ID） |
| iss | 签发者 |
| iat | 签发时间 |
| exp | 过期时间 |
| userId | 用户ID |
| username | 用户名 |
| roles | 角色列表 |
| tokenType | Token类型（access/refresh） |

### Token有效期

| Token类型 | 默认有效期 | 记住我模式 |
|-----------|-----------|-----------|
| Access Token | 2小时（7200秒） | 7天（604800秒） |
| Refresh Token | 7天（604800秒） | 7天（604800秒） |

### Token使用方式

在请求头中携带Token：

```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

## 网关认证流程

```
请求进入网关
      │
      ▼
判断是否白名单路径 ──是──▶ 直接放行
      │
      否
      │
      ▼
获取Authorization请求头
      │
      ▼
验证Token格式 ──无效──▶ 返回401
      │
      有效
      │
      ▼
验证Token签名和有效期 ──无效──▶ 返回401
      │
      有效
      │
      ▼
验证Token类型（必须是Access Token） ──无效──▶ 返回401
      │
      有效
      │
      ▼
检查Token是否在黑名单中 ──在──▶ 返回401
      │
      不在
      │
      ▼
提取用户信息，添加到请求头
      │
      ▼
转发到下游服务
```

### 白名单路径

以下路径无需认证即可访问：

| 路径 | 说明 |
|------|------|
| `/api/auth/login` | 登录接口 |
| `/api/auth/refresh` | 刷新Token接口 |
| `/api/auth/captcha` | 验证码接口（预留） |
| `/actuator/**` | 健康检查接口 |
| `/swagger-ui/**` | Swagger文档 |
| `/swagger-resources/**` | Swagger资源 |
| `/v2/api-docs` | API文档 |
| `/v3/api-docs/**` | API文档 |
| `/webjars/**` | 静态资源 |
| `/doc.html` | Knife4j文档 |

---

## 配置说明

### cloud-auth配置（Nacos: cloud-auth-dev.yml）

```yaml
spring:
  redis:
    host: 127.0.0.1
    port: 6379
    password: 
    database: 0

jwt:
  secret: CloudStudySecretKey2024!@#$%^&*()_+
  issuer: cloud-auth
  access-token-expire: 7200
  access-token-expire-remember: 604800
  refresh-token-expire: 604800

logging:
  level:
    com.cloud.auth: debug
```

### 网关路由配置（cloud-gateway-dev.yml）

```yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true
        routes:
          # 认证模块
          - id: cloud_auth
            uri: lb://cloud-auth
            predicates:
              - Path=/api/auth/**,/api/user/**
            order: 0
```

### Redis Key说明

| Key格式 | 说明 | 有效期 |
|---------|------|--------|
| `auth:access_token:{userId}` | 用户Access Token | 同Token有效期 |
| `auth:refresh_token:{userId}` | 用户Refresh Token | 7天 |
| `auth:token_blacklist:{token}` | Token黑名单 | Token剩余有效期 |
| `auth:user_info:{userId}` | 用户信息缓存 | 自定义 |
| `auth:login_fail:{username}` | 登录失败次数 | 30分钟 |

---

## 快速开始

### 1. 添加Maven依赖

在 `cloud-auth/pom.xml` 中添加：

```xml
<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Spring Web -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>

<!-- Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

在 `cloud-service-common/pom.xml` 中添加：

```xml
<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

### 2. 配置Nacos

将 `config/cloud-auth-dev.yml` 配置导入Nacos配置中心。

### 3. 配置网关路由

确保 `config/cloud-gateway-dev.yml` 中包含认证服务路由配置。

### 4. 启动服务

按以下顺序启动服务：

1. Nacos
2. Redis
3. cloud-gateway-client
4. cloud-auth
5. 其他业务服务

### 5. 测试登录

```bash
# 登录
curl -X POST http://localhost:51201/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456","rememberMe":false}'

# 获取用户信息
curl -X POST http://localhost:51201/api/user/info \
  -H "Authorization: Bearer <your_token>"

# 刷新Token
curl -X POST http://localhost:51201/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"<your_refresh_token>"}'

# 登出
curl -X POST http://localhost:51201/api/auth/logout \
  -H "Authorization: Bearer <your_token>"
```

---

## 安全建议

### 1. 密钥管理

- **不要硬编码密钥**：JWT签名密钥应从配置中心（Nacos）读取
- **使用复杂密钥**：密钥长度至少256位
- **定期轮换密钥**：建议每3-6个月更换一次

### 2. Token安全

- **使用HTTPS**：生产环境必须使用HTTPS传输
- **Token黑名单**：登出时将Token加入黑名单
- **适当的有效期**：不要设置过长的Token有效期

### 3. 防暴力破解

- **登录失败限制**：5次失败后锁定30分钟
- **验证码**：可添加图形验证码或短信验证码
- **IP限制**：可限制单IP请求频率

### 4. 前端安全

- **XSS防护**：推荐使用httpOnly Cookie存储Token
- **CSRF防护**：使用CSRF Token
- **敏感信息**：不要在Token中存储敏感信息

### 5. 密码安全

- **加密传输**：前端可使用RSA加密密码后传输
- **强密码策略**：要求密码包含大小写字母、数字、特殊字符
- **BCrypt加密**：使用BCrypt算法存储密码

---

## 前端集成示例

### 请求拦截器

```typescript
import axios from 'axios'

const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 10000
})

// 请求拦截器
service.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

// 响应拦截器
service.interceptors.response.use(
  (response) => response.data,
  async (error) => {
    if (error.response?.status === 401) {
      // 尝试刷新Token
      const refreshToken = localStorage.getItem('refreshToken')
      if (refreshToken) {
        try {
          const res = await refreshTokenApi(refreshToken)
          localStorage.setItem('token', res.data.token)
          // 重试原请求
          return service(error.config)
        } catch {
          // 刷新失败，跳转登录
          localStorage.removeItem('token')
          localStorage.removeItem('refreshToken')
          window.location.href = '/login'
        }
      } else {
        window.location.href = '/login'
      }
    }
    return Promise.reject(error)
  }
)
```

### 登录示例

```typescript
interface LoginParams {
  username: string
  password: string
  rememberMe?: boolean
}

interface LoginResult {
  token: string
  refreshToken: string
  expiresIn: number
  tokenType: string
}

export async function login(params: LoginParams): Promise<LoginResult> {
  const response = await service.post('/api/auth/login', params)
  if (response.code === 0) {
    // 存储Token
    localStorage.setItem('token', response.data.token)
    localStorage.setItem('refreshToken', response.data.refreshToken)
  }
  return response
}
```

---

## 常见问题

### Q1: Token过期后如何处理？

**A:** 前端在请求拦截器中检测401状态码，使用Refresh Token自动刷新。如果Refresh Token也过期，则跳转到登录页。

### Q2: 如何实现多设备登录限制？

**A:** 可以在Redis中存储用户最新的Token，每次登录更新Token，旧Token自动失效。

### Q3: 如何防止Token被盗用？

**A:** 
- 使用HTTPS传输
- 设置合理的Token有效期
- 实现Token刷新机制
- 绑定用户IP或设备指纹
- 实现异常登录检测

### Q4: 登录失败多次被锁定怎么办？

**A:** 默认5次失败后锁定30分钟。可以：
- 等待30分钟后自动解锁
- 管理员手动清除Redis中的锁定记录
- 通过验证码重置

### Q5: 如何扩展用户数据源？

**A:** 修改 `UserDetailsServiceImpl.java`，将模拟数据改为从数据库查询。需要：
1. 创建用户表和Mapper
2. 注入UserMapper
3. 修改 `loadUserByUsername` 方法从数据库查询

---

## 文件清单

### cloud-auth模块

```
cloud-auth/
├── src/main/java/com/cloud/auth/
│   ├── AuthApplication.java                    # 启动类
│   ├── config/
│   │   └── SecurityConfig.java                 # Security配置
│   ├── controller/
│   │   └── AuthController.java                 # 认证控制器
│   ├── model/
│   │   ├── dto/
│   │   │   ├── LoginRequest.java               # 登录请求DTO
│   │   │   ├── LoginResponse.java              # 登录响应DTO
│   │   │   ├── RefreshTokenRequest.java        # 刷新Token请求DTO
│   │   │   └── RefreshTokenResponse.java       # 刷新Token响应DTO
│   │   └── entity/
│   │       └── SysUser.java                    # 系统用户实体
│   ├── security/
│   │   ├── JwtAuthenticationEntryPoint.java    # 认证入口点
│   │   ├── JwtAuthenticationFilter.java        # JWT过滤器
│   │   └── LoginUser.java                      # 登录用户详情
│   └── service/
│       ├── AuthService.java                    # 认证服务
│       └── UserDetailsServiceImpl.java         # 用户详情服务
└── src/main/resources/
    └── bootstrap.yml                           # 启动配置
```

### cloud-service-admin模块（用户相关）

```
cloud-service-admin/
├── src/main/java/com/cloud/admin/
│   ├── controller/
│   │   └── UserController.java                 # 用户控制器
│   ├── model/
│   │   ├── dto/
│   │   │   └── UserInfoResponse.java           # 用户信息响应DTO
│   │   └── entity/
│   │       └── SysUser.java                    # 系统用户实体
│   └── service/
│       └── SysUserService.java                 # 用户服务
└── src/main/resources/
    └── bootstrap.yml                           # 启动配置
```

### cloud-gateway-client模块

```
cloud-gateway-client/
├── src/main/java/com/cloud/gateway/
│   ├── GatewayClientApplication.java           # 启动类
│   ├── config/
│   │   └── GatewayConfig.java                  # 网关配置
│   └── filter/
│       ├── JwtAuthenticationFilter.java        # JWT认证过滤器
│       └── TracerFilter.java                   # 链路追踪过滤器
└── src/main/resources/
    └── bootstrap.yml                           # 启动配置
```

### cloud-service-common模块（新增）

```
cloud-service-common/
└── src/main/java/com/cloud/common/
    ├── constants/
    │   ├── AuthConstants.java                  # 认证常量
    │   └── RedisConstants.java                 # Redis常量（更新）
    └── utils/
        └── JwtUtil.java                        # JWT工具类
```

### 配置文件

```
config/
├── cloud-auth-dev.yml                          # 认证服务配置
└── cloud-gateway-dev.yml                       # 网关配置（更新）
```

---

## 更新日志

| 版本 | 日期 | 说明 |
|------|------|------|
| 1.0.0 | 2026-01-03 | 初始版本，实现基于Spring Security + JWT的单点登录功能 |

---

## 附录：响应状态码

| 状态码 | 说明 |
|--------|------|
| 0 | 请求成功 |
| -1 | 通用错误 |
| 401 | 未授权（未登录或Token无效） |
| 403 | 禁止访问（无权限） |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

