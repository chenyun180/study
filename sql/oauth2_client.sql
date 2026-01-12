-- ========================================
-- OAuth2 客户端信息表
-- 用于存储OAuth2客户端应用的配置信息
-- ========================================

-- 删除已存在的表（如果需要重建）
-- DROP TABLE IF EXISTS oauth_client_details;

-- 创建OAuth2客户端信息表
CREATE TABLE IF NOT EXISTS `oauth_client_details` (
    `client_id` VARCHAR(256) NOT NULL COMMENT '客户端ID',
    `resource_ids` VARCHAR(256) DEFAULT NULL COMMENT '资源ID列表，多个用逗号分隔',
    `client_secret` VARCHAR(256) DEFAULT NULL COMMENT '客户端密钥（加密后）',
    `scope` VARCHAR(256) DEFAULT NULL COMMENT '授权范围，如read,write',
    `authorized_grant_types` VARCHAR(256) DEFAULT NULL COMMENT '授权类型，如password,authorization_code,refresh_token,client_credentials',
    `web_server_redirect_uri` VARCHAR(256) DEFAULT NULL COMMENT '授权码模式回调地址',
    `authorities` VARCHAR(256) DEFAULT NULL COMMENT '客户端权限',
    `access_token_validity` INT(11) DEFAULT NULL COMMENT 'Access Token有效期（秒）',
    `refresh_token_validity` INT(11) DEFAULT NULL COMMENT 'Refresh Token有效期（秒）',
    `additional_information` VARCHAR(4096) DEFAULT NULL COMMENT '额外信息（JSON格式）',
    `autoapprove` VARCHAR(256) DEFAULT NULL COMMENT '是否自动批准授权（true/false或授权范围）',
    PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth2客户端信息表';

-- ========================================
-- 初始化客户端数据
-- ========================================

-- 内部Web应用客户端（密码模式）
-- 密码: cloud123 -> BCrypt加密后的值
INSERT INTO `oauth_client_details` (
    `client_id`,
    `resource_ids`,
    `client_secret`,
    `scope`,
    `authorized_grant_types`,
    `web_server_redirect_uri`,
    `authorities`,
    `access_token_validity`,
    `refresh_token_validity`,
    `additional_information`,
    `autoapprove`
) VALUES (
    'cloud-web',
    'cloud-resource',
    '$2a$10$O7hDEYQvY.s0Q0RdZt5qJOH2LWEJ7cBMgN5Y5FBVXQP5CvH8FxDYa',
    'read,write',
    'password,refresh_token',
    NULL,
    NULL,
    7200,
    604800,
    '{"name":"Cloud Web应用","description":"内部Web管理系统"}',
    'true'
);

-- 移动端应用客户端（密码模式）
INSERT INTO `oauth_client_details` (
    `client_id`,
    `resource_ids`,
    `client_secret`,
    `scope`,
    `authorized_grant_types`,
    `web_server_redirect_uri`,
    `authorities`,
    `access_token_validity`,
    `refresh_token_validity`,
    `additional_information`,
    `autoapprove`
) VALUES (
    'cloud-app',
    'cloud-resource',
    '$2a$10$O7hDEYQvY.s0Q0RdZt5qJOH2LWEJ7cBMgN5Y5FBVXQP5CvH8FxDYa',
    'read,write',
    'password,refresh_token',
    NULL,
    NULL,
    604800,
    2592000,
    '{"name":"Cloud移动应用","description":"移动端APP"}',
    'true'
);

-- 第三方应用客户端（授权码模式）
INSERT INTO `oauth_client_details` (
    `client_id`,
    `resource_ids`,
    `client_secret`,
    `scope`,
    `authorized_grant_types`,
    `web_server_redirect_uri`,
    `authorities`,
    `access_token_validity`,
    `refresh_token_validity`,
    `additional_information`,
    `autoapprove`
) VALUES (
    'third-party-demo',
    'cloud-resource',
    '$2a$10$O7hDEYQvY.s0Q0RdZt5qJOH2LWEJ7cBMgN5Y5FBVXQP5CvH8FxDYa',
    'read',
    'authorization_code,refresh_token',
    'http://localhost:8081/callback',
    NULL,
    3600,
    86400,
    '{"name":"第三方演示应用","description":"第三方应用接入示例"}',
    'false'
);

-- 服务间调用客户端（客户端模式）
INSERT INTO `oauth_client_details` (
    `client_id`,
    `resource_ids`,
    `client_secret`,
    `scope`,
    `authorized_grant_types`,
    `web_server_redirect_uri`,
    `authorities`,
    `access_token_validity`,
    `refresh_token_validity`,
    `additional_information`,
    `autoapprove`
) VALUES (
    'cloud-service',
    'cloud-resource',
    '$2a$10$O7hDEYQvY.s0Q0RdZt5qJOH2LWEJ7cBMgN5Y5FBVXQP5CvH8FxDYa',
    'service',
    'client_credentials',
    NULL,
    'ROLE_SERVICE',
    3600,
    NULL,
    '{"name":"服务间调用","description":"微服务间内部调用"}',
    'true'
);
