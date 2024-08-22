package com.brenden.cloud.test.persistence.repository;

import com.brenden.cloud.mongodb.repository.PageMongoRepository;
import com.brenden.cloud.test.persistence.model.User;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/22
 */
@Repository
public interface TestRepository extends PageMongoRepository<User, String> {
}
