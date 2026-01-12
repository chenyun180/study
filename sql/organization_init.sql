-- ====================================================
-- 组织机构管理模块 数据库初始化脚本
-- 创建时间: 2026-01-04
-- 说明: 包含公司表、部门表、岗位表、组织用户表的创建语句和初始化数据
-- ====================================================

-- ----------------------------
-- 1. 公司表 (sys_company)
-- ----------------------------
DROP TABLE IF EXISTS `sys_company`;
CREATE TABLE `sys_company` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` BIGINT NULL DEFAULT NULL COMMENT '上级公司ID',
  `code` VARCHAR(50) NOT NULL COMMENT '公司编码',
  `name` VARCHAR(100) NOT NULL COMMENT '公司名称',
  `short_name` VARCHAR(50) NULL DEFAULT NULL COMMENT '公司简称',
  `address` VARCHAR(255) NULL DEFAULT NULL COMMENT '公司地址',
  `phone` VARCHAR(20) NULL DEFAULT NULL COMMENT '联系电话',
  `email` VARCHAR(100) NULL DEFAULT NULL COMMENT '电子邮箱',
  `legal_person` VARCHAR(50) NULL DEFAULT NULL COMMENT '法人代表',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-停用',
  `sort` INT NOT NULL DEFAULT 1 COMMENT '排序号',
  `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
  `create_by` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`, `deleted`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公司表';

-- ----------------------------
-- 2. 部门表 (sys_department)
-- ----------------------------
DROP TABLE IF EXISTS `sys_department`;
CREATE TABLE `sys_department` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` BIGINT NULL DEFAULT NULL COMMENT '上级部门ID',
  `company_id` BIGINT NOT NULL COMMENT '所属公司ID',
  `code` VARCHAR(50) NOT NULL COMMENT '部门编码',
  `name` VARCHAR(100) NOT NULL COMMENT '部门名称',
  `leader_id` BIGINT NULL DEFAULT NULL COMMENT '部门负责人ID',
  `phone` VARCHAR(20) NULL DEFAULT NULL COMMENT '联系电话',
  `email` VARCHAR(100) NULL DEFAULT NULL COMMENT '电子邮箱',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-停用',
  `sort` INT NOT NULL DEFAULT 1 COMMENT '排序号',
  `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
  `create_by` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`, `deleted`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_company_id` (`company_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_dept_company` FOREIGN KEY (`company_id`) REFERENCES `sys_company` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- ----------------------------
-- 3. 岗位表 (sys_position)
-- ----------------------------
DROP TABLE IF EXISTS `sys_position`;
CREATE TABLE `sys_position` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` BIGINT NULL DEFAULT NULL COMMENT '上级岗位ID',
  `department_id` BIGINT NOT NULL COMMENT '所属部门ID',
  `code` VARCHAR(50) NOT NULL COMMENT '岗位编码',
  `name` VARCHAR(100) NOT NULL COMMENT '岗位名称',
  `level` TINYINT NULL DEFAULT NULL COMMENT '岗位级别（1-10）',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-停用',
  `sort` INT NOT NULL DEFAULT 1 COMMENT '排序号',
  `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
  `create_by` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`, `deleted`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_department_id` (`department_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_pos_department` FOREIGN KEY (`department_id`) REFERENCES `sys_department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='岗位表';

-- ----------------------------
-- 4. 组织用户表 (sys_org_user)
-- ----------------------------
DROP TABLE IF EXISTS `sys_org_user`;
CREATE TABLE `sys_org_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `position_id` BIGINT NOT NULL COMMENT '所属岗位ID',
  `code` VARCHAR(50) NOT NULL COMMENT '用户编码',
  `name` VARCHAR(100) NOT NULL COMMENT '用户姓名',
  `phone` VARCHAR(20) NULL DEFAULT NULL COMMENT '联系电话',
  `email` VARCHAR(100) NULL DEFAULT NULL COMMENT '电子邮箱',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-停用',
  `sort` INT NOT NULL DEFAULT 1 COMMENT '排序号',
  `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
  `create_by` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` VARCHAR(50) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`, `deleted`),
  KEY `idx_position_id` (`position_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_user_position` FOREIGN KEY (`position_id`) REFERENCES `sys_position` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织用户表';

-- ----------------------------
-- 初始化公司数据
-- ----------------------------
INSERT INTO `sys_company` (`id`, `parent_id`, `code`, `name`, `short_name`, `address`, `phone`, `email`, `legal_person`, `status`, `sort`, `remark`) VALUES
(1, NULL, 'RQ', '润祺智慧', '润祺', '深圳市南山区科技园', '0755-12345678', 'contact@runqi.com', '张总', 1, 1, '集团总部'),
(2, 1, 'RQ-SH', '上海分公司', '上海', '上海市浦东新区', '021-87654321', 'shanghai@runqi.com', '李总', 1, 1, NULL),
(3, 1, 'RQ-SZ', '深圳总公司', '深圳', '深圳市福田区', '0755-11111111', 'shenzhen@runqi.com', '王总', 1, 2, NULL),
(4, 1, 'RQ-CS', '长沙分公司', '长沙', '长沙市岳麓区', '0731-22222222', 'changsha@runqi.com', '陈总', 1, 3, NULL),
(5, 1, 'RQ-NJ', '南京分公司', '南京', '南京市建邺区', '025-33333333', 'nanjing@runqi.com', '刘总', 1, 4, NULL);

-- ----------------------------
-- 初始化部门数据
-- ----------------------------
INSERT INTO `sys_department` (`id`, `parent_id`, `company_id`, `code`, `name`, `leader_id`, `phone`, `email`, `status`, `sort`) VALUES
(101, NULL, 2, 'SH-RD', '研发部', NULL, '021-11111111', 'rd@shanghai.runqi.com', 1, 1),
(102, NULL, 2, 'SH-FIN', '财务部', NULL, '021-22222222', 'fin@shanghai.runqi.com', 1, 2),
(103, NULL, 3, 'SZ-RD', '研发部门', NULL, NULL, NULL, 1, 1),
(104, NULL, 3, 'SZ-FIN', '财务部门', NULL, NULL, NULL, 1, 2),
(105, NULL, 3, 'SZ-OPS', '运维部门', NULL, NULL, NULL, 1, 3),
(106, NULL, 4, 'CS-RD9', '研发九部', NULL, NULL, NULL, 1, 1),
(107, NULL, 4, 'CS-RD', '研发部门', NULL, NULL, NULL, 1, 2),
(108, 107, 4, 'CS-RD8', '研发八部', NULL, NULL, NULL, 1, 1),
(109, NULL, 5, 'NJ-RD', '研发部', NULL, NULL, NULL, 1, 1);

-- ----------------------------
-- 初始化岗位数据
-- ----------------------------
INSERT INTO `sys_position` (`id`, `parent_id`, `department_id`, `code`, `name`, `level`, `status`, `sort`) VALUES
(201, NULL, 101, 'SH-RD-TEST', '测试岗位', 3, 1, 1),
(202, NULL, 101, 'SH-RD-DEV2', '二级部门', 2, 1, 2),
(203, NULL, 101, 'SH-RD-MID', '中级', 3, 1, 3),
(204, NULL, 102, 'SH-FIN-CASHIER', '出纳', 3, 1, 1),
(205, NULL, 103, 'SZ-RD-CEO', '董事长', 1, 1, 1),
(206, NULL, 103, 'SZ-RD-PM', '项目经理', 2, 1, 2),
(207, NULL, 103, 'SZ-RD-TEST', '测试', 3, 1, 3),
(208, NULL, 106, 'CS-RD9-SENIOR', '高级研发工程师', 2, 1, 1),
(209, NULL, 108, 'CS-RD8-MGR', '经理', 2, 1, 1),
(210, NULL, 107, 'CS-OPS-POS', '运营岗位', 3, 1, 1),
(211, NULL, 103, 'SZ-233', '233', 3, 1, 4);

