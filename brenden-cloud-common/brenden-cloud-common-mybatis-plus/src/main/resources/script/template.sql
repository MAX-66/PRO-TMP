CREATE TABLE `t_temp` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `is_delete` tinyint(4) unsigned zerofill DEFAULT NULL COMMENT '逻辑删除',
                          `create_by` bigint DEFAULT NULL COMMENT '创建人',
                          `update_by` bigint DEFAULT NULL COMMENT '最后修改人',
                          `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
                          `gmt_modified` datetime DEFAULT NULL COMMENT '最后修改时间',
                          `version` int DEFAULT NULL COMMENT '乐观锁',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;