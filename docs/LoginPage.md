# 登录页面使用说明

## 页面预览

登录页面采用科技感设计风格，包含以下特性：

- 🎨 **科技感背景**: 动态网格和粒子效果
- 🔐 **安全认证**: 基于 SpringSecurity + JWT
- 📱 **响应式设计**: 适配桌面和移动端
- ✨ **优雅动画**: 卡片淡入、悬停效果
- 🎯 **表单验证**: 完整的输入验证

## 功能特性

### 1. 用户认证

- 用户名密码登录
- "记住我"功能（7天有效期）
- 表单验证
- 登录状态保持

### 2. 测试账号

| 用户名 | 密码 | 角色 | 权限 |
|--------|------|------|------|
| admin | 123456 | 管理员 | 所有权限 (*:*:*) |
| user | 123456 | 普通用户 | 部分权限 |

### 3. Mock 模式

开发环境默认启用 Mock 模式，无需后端服务即可测试：

```bash
# .env 文件
VITE_USE_MOCK=true
```

## 技术实现

### 1. 组件结构

```vue
<template>
  <div class="login-page">
    <!-- 科技感背景 -->
    <div class="tech-background">
      <div class="tech-grid"></div>
      <div class="tech-particles"></div>
    </div>

    <!-- 登录卡片 -->
    <div class="login-container">
      <div class="login-card">
        <!-- 表单内容 -->
      </div>
    </div>

    <!-- 侧边按钮 -->
    <div class="side-buttons">
      <!-- 设置、关闭按钮 -->
    </div>
  </div>
</template>
```

### 2. 登录流程

```typescript
// 1. 表单验证
await formRef.value.validate()

// 2. 调用登录 API
const response = await login({
  username: form.username,
  password: form.password,
  rememberMe: form.rememberMe
})

// 3. 存储 token
userStore.setToken(response.data.token, response.data.refreshToken)

// 4. 获取用户信息
await userStore.fetchUserInfo()

// 5. 跳转到首页
router.push('/home')
```

### 3. JWT Token 处理

#### Token 存储

```typescript
// 使用 localStorage 存储
storage.set('token', token)
storage.set('refreshToken', refreshToken)
storage.set('userInfo', userInfo)
```

#### Token 使用

```typescript
// 请求拦截器自动添加 Authorization header
service.interceptors.request.use((config) => {
  const userStore = useUserStore()
  if (userStore.token) {
    config.headers.Authorization = `Bearer ${userStore.token}`
  }
  return config
})
```

#### Token 刷新

```typescript
// 当 token 即将过期时自动刷新
if (isTokenExpiringSoon(token)) {
  const newToken = await refreshToken(refreshToken)
  userStore.setToken(newToken)
}
```

### 4. 权限管理

#### 检查权限

```typescript
// 在组件中使用
const userStore = useUserStore()

// 检查是否有指定权限
if (userStore.hasPermission('system:user:delete')) {
  // 显示删除按钮
}

// 检查是否有指定角色
if (userStore.hasRole('admin')) {
  // 显示管理员功能
}
```

#### 路由守卫

```typescript
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next('/login')
  } else {
    next()
  }
})
```

## 样式定制

### 1. 主题颜色

```scss
// 修改背景渐变
background: linear-gradient(135deg, #1e3c72 0%, #2a5298 50%, #7e22ce 100%);

// 修改按钮颜色
background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
```

### 2. 动画效果

```scss
// 卡片淡入动画
@keyframes cardFadeIn {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

// 网格移动动画
@keyframes gridMove {
  0% {
    transform: perspective(500px) rotateX(60deg) translateY(0);
  }
  100% {
    transform: perspective(500px) rotateX(60deg) translateY(50px);
  }
}
```

### 3. 响应式设计

```scss
@media (max-width: 768px) {
  .login-container {
    max-width: 90%;
  }

  .login-card {
    padding: 32px 24px;
  }
}
```

## API 对接

### 1. 开发环境（Mock）

```typescript
// .env
VITE_USE_MOCK=true
VITE_API_BASE_URL=http://localhost:8080

// Mock 数据会自动拦截请求
```

### 2. 生产环境（真实 API）

```typescript
// .env.production
VITE_USE_MOCK=false
VITE_API_BASE_URL=https://api.yourdomain.com

// 请求会发送到真实后端
```

### 3. 后端接口要求

#### 登录接口

```
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "123456",
  "rememberMe": true
}

Response:
{
  "code": 0,
  "data": {
    "token": "eyJhbGci...",
    "refreshToken": "eyJhbGci...",
    "expiresIn": 7200,
    "tokenType": "Bearer"
  },
  "msg": "登录成功"
}
```

#### 获取用户信息接口

```
POST /api/user/info
Authorization: Bearer <token>

Response:
{
  "code": 0,
  "data": {
    "id": 1,
    "name": "系统管理员",
    "email": "admin@example.com",
    "avatar": "https://...",
    "roles": ["admin", "user"],
    "permissions": ["*:*:*"]
  },
  "msg": "获取用户信息成功"
}
```

## 安全建议

### 1. 密码加密

```typescript
// 前端传输前加密（可选）
import CryptoJS from 'crypto-js'

const encryptedPassword = CryptoJS.AES.encrypt(
  password,
  publicKey
).toString()
```

### 2. HTTPS

生产环境必须使用 HTTPS：

```nginx
server {
  listen 443 ssl;
  ssl_certificate /path/to/cert.pem;
  ssl_certificate_key /path/to/key.pem;
}
```

### 3. XSS 防护

```typescript
// 使用 httpOnly Cookie 存储 token（推荐）
document.cookie = `token=${token}; HttpOnly; Secure; SameSite=Strict`

// 或使用 localStorage（需注意 XSS）
localStorage.setItem('token', token)
```

### 4. CSRF 防护

```typescript
// 添加 CSRF token
service.interceptors.request.use((config) => {
  config.headers['X-CSRF-Token'] = getCsrfToken()
  return config
})
```

## 常见问题

### Q1: 登录后刷新页面需要重新登录？

A: 检查 token 是否正确存储到 localStorage，以及 user store 是否正确初始化。

```typescript
// 在 user store 中初始化时读取 localStorage
const token = ref<string>(storage.get('token') || '')
```

### Q2: Mock 模式下登录失败？

A: 检查环境变量配置：

```bash
# .env
VITE_USE_MOCK=true
```

### Q3: 如何自定义登录页样式？

A: 修改 `src/views/login/index.vue` 中的 SCSS 样式：

```scss
.login-page {
  // 自定义背景
  background: your-custom-gradient;
}
```

### Q4: 如何添加验证码？

A: 在表单中添加验证码组件：

```vue
<el-form-item prop="captcha">
  <el-input v-model="form.captcha" placeholder="验证码">
    <template #append>
      <img :src="captchaUrl" @click="refreshCaptcha" />
    </template>
  </el-input>
</el-form-item>
```

### Q5: 如何实现记住密码功能？

A: 使用加密存储：

```typescript
if (form.rememberMe) {
  // 加密后存储
  storage.set('savedUsername', encrypt(form.username))
  storage.set('savedPassword', encrypt(form.password))
}
```

## 扩展功能

### 1. 第三方登录

```vue
<div class="social-login">
  <el-button @click="loginWithGithub">
    <el-icon><Github /></el-icon>
    GitHub 登录
  </el-button>
  <el-button @click="loginWithGoogle">
    <el-icon><Google /></el-icon>
    Google 登录
  </el-button>
</div>
```

### 2. 手机号登录

```vue
<el-tabs v-model="loginType">
  <el-tab-pane label="账号登录" name="account">
    <!-- 用户名密码表单 -->
  </el-tab-pane>
  <el-tab-pane label="手机登录" name="phone">
    <!-- 手机号验证码表单 -->
  </el-tab-pane>
</el-tabs>
```

### 3. 找回密码

```vue
<div class="login-footer">
  <el-link type="primary" @click="forgotPassword">
    忘记密码？
  </el-link>
</div>
```

## 参考资料

- [SpringSecurity 官方文档](https://spring.io/projects/spring-security)
- [JWT 官方网站](https://jwt.io/)
- [Element Plus 表单组件](https://element-plus.org/zh-CN/component/form.html)
- [Vue Router 路由守卫](https://router.vuejs.org/zh/guide/advanced/navigation-guards.html)

