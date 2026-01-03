

/**
 * 用户登录
 * 功能描述：用户通过用户名和密码登录系统，返回 JWT token
 * 入参：{ username: string, password: string, rememberMe?: boolean }
 * 返回参数：{ token: string, refreshToken: string, expiresIn: number, tokenType: string }
 * url地址：/api/auth/login
 * 请求方式：POST
 */
export function login(params: LoginParams): Promise<LoginResult> {
  return request.post('/api/auth/login', params)
}

/**
 * 获取当前登录用户信息
 * 功能描述：通过 JWT token 获取当前登录用户的详细信息
 * 入参：无（通过 Authorization header 传递 token）
 * 返回参数：{ id: number, name: string, email: string, avatar: string, roles: string[], permissions: string[] }
 * url地址：/api/user/info
 * 请求方式：POST
 */
export function getUserInfo(): Promise<UserInfo> {
  return request.post('/api/user/info')
}

/**
 * 用户登出
 * 功能描述：用户退出登录，清除服务端 token（可选）
 * 入参：无
 * 返回参数：无
 * url地址：/api/auth/logout
 * 请求方式：POST
 */
export function logout(): Promise<void> {
  return request.post('/api/auth/logout')
}

/**
 * 刷新 token
 * 功能描述：使用 refresh token 获取新的 access token
 * 入参：{ refreshToken: string }
 * 返回参数：{ token: string, expiresIn: number }
 * url地址：/api/auth/refresh
 * 请求方式：POST
 */
export function refreshToken(refreshToken: string): Promise<{ token: string; expiresIn: number }> {
  return request.post('/api/auth/refresh', { refreshToken })
}

