package com.brenden.cloud.base;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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


    private final static Integer MAX_BATCH_COUNT = 500;

    @Override
    public Integer insertBatchSomeColumn(Collection<T> entityList, int batchSize) {
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
    public T getByCode(Serializable code) {
        return getBaseMapper().findByCode(code);
    }
}
