package com.cloud.common.config.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * OAuth2资源服务器配置
 * 
 * <p>公共模块中的资源服务器配置，各微服务引用common模块后自动获得资源服务器能力。
 * 通过配置 security.oauth2.resource.enabled=true 开启资源服务器功能。</p>
 *
 * @author cloud
 */
@Slf4j
@Configuration
@EnableResourceServer
@RequiredArgsConstructor
@EnableConfigurationProperties(ResourceServerProperties.class)
@ConditionalOnProperty(prefix = "security.oauth2.resource", name = "enabled", havingValue = "true", matchIfMissing = false)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final ResourceServerProperties resourceServerProperties;

    /**
     * JWT访问令牌转换器
     *
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(resourceServerProperties.getJwtSigningKey());
        return converter;
    }

    /**
     * JWT Token存储
     *
     * @return TokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    /**
     * 远程Token校验服务（可选，用于非JWT场景）
     *
     * @return RemoteTokenServices
     */
    @Bean
    @ConditionalOnProperty(prefix = "security.oauth2.resource", name = "use-remote-token-services", havingValue = "true")
    public RemoteTokenServices remoteTokenServices() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setCheckTokenEndpointUrl(
                resourceServerProperties.getAuthServerUrl() + resourceServerProperties.getCheckTokenEndpoint());
        tokenServices.setClientId(resourceServerProperties.getClientId());
        tokenServices.setClientSecret(resourceServerProperties.getClientSecret());
        return tokenServices;
    }

    /**
     * 配置资源服务器
     *
     * @param resources ResourceServerSecurityConfigurer
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                .resourceId(resourceServerProperties.getResourceId())
                .tokenStore(tokenStore())
                .stateless(true);
        
        log.info("OAuth2资源服务器已启用，资源ID: {}", resourceServerProperties.getResourceId());
    }

    /**
     * 配置HTTP安全规则
     *
     * @param http HttpSecurity
     * @throws Exception 异常
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 获取公开接口列表
        String[] permitUrls = resourceServerProperties.getPermitUrls().toArray(new String[0]);
        
        http
                .csrf().disable()
                .authorizeRequests()
                // 公开接口
                .antMatchers(permitUrls).permitAll()
                // Swagger文档
                .antMatchers(
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/v3/api-docs/**",
                        "/webjars/**",
                        "/doc.html"
                ).permitAll()
                // Actuator健康检查
                .antMatchers("/actuator/**").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated();
    }
}
