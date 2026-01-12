package com.cloud.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * OAuth2 认证服务器配置
 * 
 * <p>配置OAuth2认证服务器，支持以下授权模式：
 * <ul>
 *     <li>password - 密码模式，适用于内部应用</li>
 *     <li>authorization_code - 授权码模式，适用于第三方应用接入</li>
 *     <li>refresh_token - 刷新令牌</li>
 *     <li>client_credentials - 客户端模式，适用于服务间调用</li>
 * </ul>
 * </p>
 *
 * @author cloud
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final RedisConnectionFactory redisConnectionFactory;
    private final DataSource dataSource;
    private final PasswordEncoder passwordEncoder;
    private final OAuth2Properties oAuth2Properties;

    /**
     * 配置Token存储方式
     * 使用Redis存储Token，支持分布式环境
     *
     * @return TokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix("oauth2:token:");
        return redisTokenStore;
    }

    /**
     * JWT访问令牌转换器
     * 用于生成和验证JWT格式的访问令牌
     *
     * @return JwtAccessTokenConverter
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(oAuth2Properties.getJwtSigningKey());
        return converter;
    }

    /**
     * 自定义Token增强器
     * 可以在Token中添加额外的用户信息
     *
     * @return TokenEnhancer
     */
    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    /**
     * 客户端详情服务
     * 从数据库加载客户端信息
     *
     * @return ClientDetailsService
     */
    @Bean
    public ClientDetailsService jdbcClientDetailsService() {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    /**
     * 配置令牌服务
     *
     * @return DefaultTokenServices
     */
    @Bean
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        tokenServices.setReuseRefreshToken(false);
        tokenServices.setClientDetailsService(jdbcClientDetailsService());
        
        // 配置Token增强链
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));
        tokenServices.setTokenEnhancer(enhancerChain);
        
        // 设置Token有效期
        tokenServices.setAccessTokenValiditySeconds(oAuth2Properties.getAccessTokenValiditySeconds());
        tokenServices.setRefreshTokenValiditySeconds(oAuth2Properties.getRefreshTokenValiditySeconds());
        
        return tokenServices;
    }

    /**
     * 配置认证服务器安全约束
     *
     * @param security AuthorizationServerSecurityConfigurer
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security
                // 允许客户端表单认证
                .allowFormAuthenticationForClients()
                // 开放/oauth/token_key端点
                .tokenKeyAccess("permitAll()")
                // 开放/oauth/check_token端点
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * 配置客户端详情服务
     * 从数据库读取客户端配置
     *
     * @param clients ClientDetailsServiceConfigurer
     * @throws Exception 异常
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService());
    }

    /**
     * 配置授权服务器端点
     *
     * @param endpoints AuthorizationServerEndpointsConfigurer
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        // 配置Token增强链
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        enhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), jwtAccessTokenConverter()));
        
        endpoints
                // 认证管理器 - 支持password模式
                .authenticationManager(authenticationManager)
                // 用户详情服务 - 支持refresh_token模式
                .userDetailsService(userDetailsService)
                // Token存储
                .tokenStore(tokenStore())
                // Token增强
                .tokenEnhancer(enhancerChain)
                // 访问令牌转换器
                .accessTokenConverter(jwtAccessTokenConverter())
                // 允许POST提交
                .allowedTokenEndpointRequestMethods(org.springframework.http.HttpMethod.POST);
    }
}
