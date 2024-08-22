package com.brenden.cloud.mongodb.config;

import com.brenden.cloud.mongodb.base.BaseMongoRepository;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/22
 */
@AutoConfiguration
@EnableMongoRepositories(basePackages = "com.brenden.cloud.**.repository", repositoryBaseClass = BaseMongoRepository.class)
public class MongoAutoConfig {
}
