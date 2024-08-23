package com.brenden.cloud.mongodb.repository;

import com.brenden.cloud.base.entity.PageEntity;
import com.brenden.cloud.mongodb.entity.MongoPageEntity;
import com.brenden.cloud.mongodb.entity.QueryEntity;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface PageMongoRepository<T extends Serializable, ID> extends MongoRepository<T, ID> {

    /**
     * 分页查询
     * @param page 分页参数
     * @return MongoPageEntity
     */
    MongoPageEntity<T> queryPage(PageEntity page);

    /**
     * 分页查询
     * @param page 分页参数
     * @param query 查询对象
     * @return MongoPageEntity
     */
    MongoPageEntity<T> queryPage(PageEntity page, Query query);

    /**
     * 分页查询
     * @param page 分页参数
     * @param queryEntityList 查询参数
     * @return MongoPageEntity
     */
    MongoPageEntity<T> queryPage(PageEntity page, List<QueryEntity> queryEntityList);

}
