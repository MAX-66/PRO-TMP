package com.brenden.cloud.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

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
public class BaseServiceImpl<M extends BaseDao<T>, T> extends ServiceImpl<M, T> {

    @Override
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            log.info("entityList is empty!");
            return true;
        }
        int size = entityList.size();
        if (size <= batchSize) {
            return getBaseMapper().insertBatchSomeColumn(entityList) == size;
        }
        int batchCount = (size + batchSize - 1) / batchSize; // 计算批次数量
        int totalInserted = 0; // 累积插入计数器
        List<T> batchList = new ArrayList<>(batchSize);

        for (int i = 0; i < batchCount; i++) {
            batchList.clear();
            int start = i * batchSize;
            int end = Math.min(start + batchSize, size);
            batchList.addAll(new ArrayList<>(entityList).subList(start, end));

            int inserted = getBaseMapper().insertBatchSomeColumn(batchList);
            if (inserted != batchList.size()) {
                return false;
            }

            totalInserted += inserted;
        }

        return totalInserted == size;
    }
}
