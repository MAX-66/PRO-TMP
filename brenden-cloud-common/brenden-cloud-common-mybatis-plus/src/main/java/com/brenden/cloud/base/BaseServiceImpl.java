package com.brenden.cloud.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/7
 */
@Slf4j
public class BaseServiceImpl<M extends BaseDao<T>, T> extends ServiceImpl<M, T> implements BaseService<T>{


    private static final Integer MAX_BATCH_COUNT = 500;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBatchSomeColumn(Collection<T> entityList) {
        return this.insertBatchSomeColumn(entityList, MAX_BATCH_COUNT);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBatchSomeColumn(Collection<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            log.info("entityList is empty!");
            return 0;
        }
        if (batchSize == 0) {
            batchSize = MAX_BATCH_COUNT;
        }
        int size = entityList.size();
        if (size <= batchSize) {
            return getBaseMapper().insertBatchSomeColumn(entityList);
        }

        int batchCount = (size + batchSize - 1) / batchSize; // 计算批次数量
        int totalInserted = 0; // 累积插入计数器
        List<T> batchList = new ArrayList<>(batchSize);
        for (int i = 0; i < batchCount; i++) {
            batchList.clear();
            int start = i * batchSize;
            int end = Math.min(start + batchSize, size);
            batchList.addAll(new ArrayList<>(entityList).subList(start, end));
            totalInserted += getBaseMapper().insertBatchSomeColumn(batchList);
        }
        return totalInserted;
    }

    @Override
    public boolean exists(Wrapper<T> queryWrapper) {
        return SqlHelper.retBool(getBaseMapper().isExists(queryWrapper));
    }

    @Override
    public T getByCode(Serializable code) {
        return getBaseMapper().findByCode(code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByCode(Serializable code) {
        return SqlHelper.retBool(getBaseMapper().deleteByCode(code));
    }

    @Override
    public T getByFunction(SFunction<T, Object> column, Object val) {
        return lambdaQuery()
                .eq(column, val)
                .one();
    }

    @Override
    public List<T> listByFunction(SFunction<T, Object> column, Object... values) {
        return lambdaQuery()
                .in(column, values)
                .list();
    }

    @Override
    public List<T> listByFunction(SFunction<T, Object> column, Collection<?> coll) {
        return lambdaQuery()
                .in(column, coll)
                .list();
    }



}
