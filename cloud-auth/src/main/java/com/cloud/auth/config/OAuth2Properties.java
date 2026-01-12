package com.cloud.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * OAuth2配置属性
 *
 * @author cloud
 */
@Data
@Component
@ConfigurationProperties(prefix = "oauth2")
public class OAuth2Properties {

    /**
     * JWT签名密钥
     */
    private String jwtSigningKey = "CloudStudyOAuth2SecretKey2024";

    /**
     * Access Token有效期（秒），默认2小时
     */
    private Integer accessTokenValiditySeconds = 7200;

    /**
     * Refresh Token有效期（秒），默认7天
     */
    private Integer refreshTokenValiditySeconds = 604800;
}
