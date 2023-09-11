/*
 Navicat Premium Data Transfer

 Source Server         : mysql-localhost
 Source Server Type    : MySQL
 Source Server Version : 80033
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 80033
 File Encoding         : 65001

 Date: 11/09/2023 16:04:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth2_authorization
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization`;
CREATE TABLE `oauth2_authorization` (
  `id` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `registered_client_id` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `principal_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL,
  `authorization_grant_type` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `authorized_scopes` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `attributes` blob,
  `state` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `authorization_code_value` blob,
  `authorization_code_issued_at` timestamp NULL DEFAULT NULL,
  `authorization_code_expires_at` timestamp NULL DEFAULT NULL,
  `authorization_code_metadata` blob,
  `access_token_value` blob,
  `access_token_issued_at` timestamp NULL DEFAULT NULL,
  `access_token_expires_at` timestamp NULL DEFAULT NULL,
  `access_token_metadata` blob,
  `access_token_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `access_token_scopes` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `oidc_id_token_value` blob,
  `oidc_id_token_issued_at` timestamp NULL DEFAULT NULL,
  `oidc_id_token_expires_at` timestamp NULL DEFAULT NULL,
  `oidc_id_token_metadata` blob,
  `refresh_token_value` blob,
  `refresh_token_issued_at` timestamp NULL DEFAULT NULL,
  `refresh_token_expires_at` timestamp NULL DEFAULT NULL,
  `refresh_token_metadata` blob,
  `user_code_value` blob,
  `user_code_issued_at` timestamp NULL DEFAULT NULL,
  `user_code_expires_at` timestamp NULL DEFAULT NULL,
  `user_code_metadata` blob,
  `device_code_value` blob,
  `device_code_issued_at` timestamp NULL DEFAULT NULL,
  `device_code_expires_at` timestamp NULL DEFAULT NULL,
  `device_code_metadata` blob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for oauth2_authorization_1.0.1
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization_1.0.1`;
CREATE TABLE `oauth2_authorization_1.0.1` (
  `id` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `registered_client_id` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `principal_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL,
  `authorization_grant_type` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `authorized_scopes` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `attributes` blob,
  `state` varchar(500) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `authorization_code_value` blob,
  `authorization_code_issued_at` timestamp NULL DEFAULT NULL,
  `authorization_code_expires_at` timestamp NULL DEFAULT NULL,
  `authorization_code_metadata` blob,
  `access_token_value` blob,
  `access_token_issued_at` timestamp NULL DEFAULT NULL,
  `access_token_expires_at` timestamp NULL DEFAULT NULL,
  `access_token_metadata` blob,
  `access_token_type` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `access_token_scopes` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `oidc_id_token_value` blob,
  `oidc_id_token_issued_at` timestamp NULL DEFAULT NULL,
  `oidc_id_token_expires_at` timestamp NULL DEFAULT NULL,
  `oidc_id_token_metadata` blob,
  `refresh_token_value` blob,
  `refresh_token_issued_at` timestamp NULL DEFAULT NULL,
  `refresh_token_expires_at` timestamp NULL DEFAULT NULL,
  `refresh_token_metadata` blob,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for oauth2_authorization_consent
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_authorization_consent`;
CREATE TABLE `oauth2_authorization_consent` (
  `registered_client_id` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `principal_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL,
  `authorities` varchar(1000) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`registered_client_id`,`principal_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for oauth2_registered_client
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_registered_client`;
CREATE TABLE `oauth2_registered_client` (
  `id` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `client_id` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `client_id_issued_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `client_secret` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `client_secret_expires_at` timestamp NULL DEFAULT NULL,
  `client_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL,
  `client_authentication_methods` varchar(1000) COLLATE utf8mb4_general_ci NOT NULL,
  `authorization_grant_types` varchar(1000) COLLATE utf8mb4_general_ci NOT NULL,
  `redirect_uris` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `post_logout_redirect_uris` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `scopes` varchar(1000) COLLATE utf8mb4_general_ci NOT NULL,
  `client_settings` varchar(2000) COLLATE utf8mb4_general_ci NOT NULL,
  `token_settings` varchar(2000) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for oauth2_registered_client_1.0.1
-- ----------------------------
DROP TABLE IF EXISTS `oauth2_registered_client_1.0.1`;
CREATE TABLE `oauth2_registered_client_1.0.1` (
  `id` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `client_id` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
  `client_id_issued_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `client_secret` varchar(200) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `client_secret_expires_at` timestamp NULL DEFAULT NULL,
  `client_name` varchar(200) COLLATE utf8mb4_general_ci NOT NULL,
  `client_authentication_methods` varchar(1000) COLLATE utf8mb4_general_ci NOT NULL,
  `authorization_grant_types` varchar(1000) COLLATE utf8mb4_general_ci NOT NULL,
  `redirect_uris` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `scopes` varchar(1000) COLLATE utf8mb4_general_ci NOT NULL,
  `client_settings` varchar(2000) COLLATE utf8mb4_general_ci NOT NULL,
  `token_settings` varchar(2000) COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色名',
  `status` int DEFAULT NULL COMMENT '状态 1启用 2禁用',
  `is_delete` tinyint(1) unsigned zerofill DEFAULT NULL COMMENT '逻辑删除',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '最后修改人',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后修改时间',
  `version` int DEFAULT NULL COMMENT '乐观锁',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统角色表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(256) COLLATE utf8mb4_general_ci NOT NULL COMMENT '登陆用户名',
  `password` varchar(1024) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '密码',
  `realName` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '真实姓名',
  `email` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(64) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号码',
  `sex` char(1) COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `status` int DEFAULT NULL COMMENT '用户状态 1、正常 2、冻结',
  `expires_date` datetime DEFAULT NULL COMMENT '账号过期时间',
  `is_delete` tinyint(1) unsigned zerofill DEFAULT NULL COMMENT '逻辑删除',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '最后修改人',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后修改时间',
  `version` int DEFAULT NULL COMMENT '乐观锁',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统用户表';

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `role_id` bigint DEFAULT NULL COMMENT '角色id',
  `is_delete` tinyint(1) unsigned zerofill DEFAULT NULL COMMENT '逻辑删除',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '最后修改人',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后修改时间',
  `version` int DEFAULT NULL COMMENT '乐观锁',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户角色关联表';

-- ----------------------------
-- Table structure for t_temp
-- ----------------------------
DROP TABLE IF EXISTS `t_temp`;
CREATE TABLE `t_temp` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_delete` tinyint(1) unsigned zerofill DEFAULT NULL COMMENT '逻辑删除',
  `create_by` bigint DEFAULT NULL COMMENT '创建人',
  `update_by` bigint DEFAULT NULL COMMENT '最后修改人',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '最后修改时间',
  `version` int DEFAULT NULL COMMENT '乐观锁',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS = 1;
