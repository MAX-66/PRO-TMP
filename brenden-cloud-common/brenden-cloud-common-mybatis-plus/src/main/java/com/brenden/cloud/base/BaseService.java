package com.brenden.cloud.base;

import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/7
 */
public interface BaseService<T> extends IService<T> {

    Integer insertBatchSomeColumn(Collection<T> entityList, int batchSize);


}
