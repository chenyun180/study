package com.cloud.auth.security;

import com.cloud.auth.model.entity.SysUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Security用户详情类
 *
 * @author cloud
 */
@Getter
public class LoginUser implements UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private final Long userId;

    /**
     * 用户名
     */
    private final String username;

    /**
     * 密码
     */
    private final String password;

    /**
     * 用户名称
     */
    private final String name;

    /**
     * 邮箱
     */
    private final String email;

    /**
     * 头像
     */
    private final String avatar;

    /**
     * 角色列表
     */
    private final List<String> roles;

    /**
     * 权限列表
     */
    private final List<String> permissions;

    /**
     * 账户状态
     */
    private final boolean enabled;

    /**
     * 权限集合
     */
    private final Collection<? extends GrantedAuthority> authorities;

    /**
     * 构造函数
     *
     * @param user 系统用户
     */
    public LoginUser(SysUser user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.name = user.getName();
        this.email = user.getEmail();
        this.avatar = user.getAvatar();
        this.roles = user.getRoles();
        this.permissions = user.getPermissions();
        this.enabled = user.getStatus() != null && user.getStatus() == 1;

        // 构建权限集合：包含角色和权限
        this.authorities = buildAuthorities(user.getRoles(), user.getPermissions());
    }

    /**
     * 构建权限集合
     *
     * @param roles       角色列表
     * @param permissions 权限列表
     * @return 权限集合
     */
    private Collection<? extends GrantedAuthority> buildAuthorities(List<String> roles, List<String> permissions) {
        // 角色需要加上 ROLE_ 前缀
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        // 添加权限
        if (permissions != null) {
            permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .forEach(authorities::add);
        }

        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

