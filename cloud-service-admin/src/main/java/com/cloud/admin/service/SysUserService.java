package com.cloud.admin.service;

import cn.hutool.core.util.StrUtil;
import com.cloud.admin.model.dto.UserInfoResponse;
import com.cloud.admin.model.entity.SysUser;
import com.cloud.common.model.base.ResultEntity;
import com.cloud.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统用户服务类
 *
 * @author cloud
 */
@Slf4j
@Service
public class SysUserService {

    /**
     * 模拟用户数据（实际项目应该从数据库查询）
     */
    private static final Map<Long, SysUser> MOCK_USERS = new HashMap<>();

    static {
        // 使用BCrypt加密的密码：123456
        String encodedPassword = new BCryptPasswordEncoder().encode("123456");

        // 管理员用户
        MOCK_USERS.put(1L, SysUser.builder()
                .id(1L)
                .username("admin")
                .password(encodedPassword)
                .name("系统管理员")
                .email("admin@example.com")
                .avatar("https://avatars.githubusercontent.com/u/1?v=4")
                .status(1)
                .roles(Arrays.asList("admin", "user"))
                .permissions(Arrays.asList("*:*:*"))
                .build());

        // 普通用户
        MOCK_USERS.put(2L, SysUser.builder()
                .id(2L)
                .username("user")
                .password(encodedPassword)
                .name("普通用户")
                .email("user@example.com")
                .avatar("https://avatars.githubusercontent.com/u/2?v=4")
                .status(1)
                .roles(Arrays.asList("user"))
                .permissions(Arrays.asList("system:user:view", "system:user:edit"))
                .build());
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public SysUser getUserById(Long userId) {
        return MOCK_USERS.get(userId);
    }

    /**
     * 获取当前登录用户信息
     *
     * @param token Access Token（包含Bearer前缀）
     * @return 用户信息响应
     */
    public ResultEntity<UserInfoResponse> getUserInfo(String token) {
        if (StrUtil.isBlank(token)) {
            return ResultEntity.failed("未登录或登录已过期");
        }

        // 提取实际Token
        token = JwtUtil.extractToken(token);

        // 验证Token
        if (!JwtUtil.validateToken(token)) {
            return ResultEntity.failed("Token无效或已过期");
        }

        // 获取用户ID
        Long userId = JwtUtil.getUserId(token);
        if (userId == null) {
            return ResultEntity.failed("无法获取用户信息");
        }

        // 获取用户详情
        SysUser user = getUserById(userId);
        if (user == null) {
            return ResultEntity.failed("用户不存在");
        }

        UserInfoResponse response = UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .roles(user.getRoles())
                .permissions(user.getPermissions())
                .build();

        return ResultEntity.ok(response, "获取用户信息成功");
    }
}

