package com.cloud.auth.service;

import cn.hutool.core.collection.CollUtil;
import com.cloud.auth.model.entity.SysUser;
import com.cloud.auth.security.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户详情服务实现类
 * 注意：实际项目中应该从数据库查询用户信息，这里使用模拟数据演示
 *
 * @author cloud
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    /**
     * 模拟用户数据（实际项目应该从数据库查询）
     */
    private static final Map<String, SysUser> MOCK_USERS = new HashMap<>();

    static {
        // 使用BCrypt加密的密码：123456
        String encodedPassword = new BCryptPasswordEncoder().encode("123456");

        // 管理员用户
        MOCK_USERS.put("admin", SysUser.builder()
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
        MOCK_USERS.put("user", SysUser.builder()
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
     * 根据用户名加载用户信息
     *
     * @param username 用户名
     * @return 用户详情
     * @throws UsernameNotFoundException 用户不存在异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("开始加载用户信息，用户名: {}", username);

        // 从模拟数据中获取用户（实际项目应该从数据库查询）
        SysUser user = MOCK_USERS.get(username);

        if (user == null) {
            log.warn("用户不存在: {}", username);
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        if (user.getStatus() == null || user.getStatus() != 1) {
            log.warn("用户已被禁用: {}", username);
            throw new UsernameNotFoundException("用户已被禁用");
        }

        log.info("用户信息加载成功: {}", username);
        return new LoginUser(user);
    }

    /**
     * 根据用户ID获取用户信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public SysUser getUserById(Long userId) {
        return MOCK_USERS.values().stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .orElse(null);
    }
}

