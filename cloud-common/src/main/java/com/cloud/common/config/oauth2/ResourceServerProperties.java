package com.cloud.common.config.oauth2;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 资源服务器配置属性
 *
 * @author cloud
 */
@Data
@ConfigurationProperties(prefix = "security.oauth2.resource")
public class ResourceServerProperties {

    /**
     * 是否启用资源服务器
     */
    private Boolean enabled = true;

    /**
     * 资源ID
     */
    private String resourceId = "cloud-resource";

    /**
     * 认证服务器地址
     */
    private String authServerUrl = "http://localhost:8080";

    /**
     * Token校验端点
     */
    private String checkTokenEndpoint = "/oauth/check_token";

    /**
     * 客户端ID（用于校验Token）
     */
    private String clientId;

    /**
     * 客户端密钥（用于校验Token）
     */
    private String clientSecret;

    /**
     * JWT签名密钥（与认证服务器一致）
     */
    private String jwtSigningKey = "CloudStudyOAuth2SecretKey2024";

    /**
     * 公开接口列表（不需要认证）
     */
    private List<String> permitUrls = new ArrayList<>();
}
