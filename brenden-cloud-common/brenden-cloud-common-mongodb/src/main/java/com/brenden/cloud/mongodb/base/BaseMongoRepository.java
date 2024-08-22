package com.brenden.cloud.mongodb.base;

import com.brenden.cloud.base.entity.PageEntity;
import com.brenden.cloud.mongodb.entity.MongoPageEntity;
import com.brenden.cloud.mongodb.entity.QueryEntity;
import com.brenden.cloud.mongodb.repository.PageMongoRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 分页
 * </p>
 *
 * @author lxq
 * @since 2024/8/22
 */
public class BaseMongoRepository<T, ID> extends SimpleMongoRepository<T, ID> implements PageMongoRepository<T, ID> {

    private final MongoEntityInformation<T, ID> entityInformation;
    private final MongoOperations mongoOperations;

    /**
     * Creates a new {@link SimpleMongoRepository} for the given {@link MongoEntityInformation} and {@link MongoTemplate}.
     *
     * @param metadata        must not be {@literal null}.
     * @param mongoOperations must not be {@literal null}.
     */
    public BaseMongoRepository(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
        this.entityInformation = metadata;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public MongoPageEntity<T> queryPage(PageEntity page) {
        return queryPage(page, new Query());
    }

    @Override
    public MongoPageEntity<T> queryPage(PageEntity page, Query query) {
        Assert.notNull(page, "page must not be null");
        long count = count();
        Pageable pageRequest = getPageable(page);
        query.with(pageRequest);
        List<T> list = mongoOperations.find(query, entityInformation.getJavaType(), entityInformation.getCollectionName());
        return new MongoPageEntity<>(list, pageRequest, count);
    }

    private Pageable getPageable(PageEntity page) {
        // skip 从 0 开始， 第一页 skip(0) 第二页 skip(10)...
        return PageRequest.of(Math.toIntExact(page.getPageNum() - 1), Math.toIntExact(page.getPageSize()), getSort(page));
    }

    @Override
    public MongoPageEntity<T> queryPage(PageEntity page, List<QueryEntity> queryEntityList) {
        Query query = new Query();
        addCriteria(query, queryEntityList);
        return queryPage(page, query);
    }


    /**
     * 添加查询条件
     * @param query 查询query
     * @param queryEntityList  条件
     */
    private void addCriteria(Query query, List<QueryEntity> queryEntityList) {
        if (CollectionUtils.isEmpty(queryEntityList)) {
            return;
        }
        queryEntityList.forEach(queryEntity -> query.addCriteria(Criteria.where(queryEntity.getField()).is(queryEntity.getValue())));
    }

    /**
     * 排序条件
     * @param page 分页参数
     * @return Sort
     */
    private Sort getSort(PageEntity page) {
        if (StringUtils.isBlank(page.getOrderBy())) {
            return Sort.unsorted();
        }
        return Sort.by(new Sort.Order(page.getSort() ? Sort.Direction.ASC : Sort.Direction.DESC, page.getOrderBy(), Sort.NullHandling.NATIVE));
    }

}
