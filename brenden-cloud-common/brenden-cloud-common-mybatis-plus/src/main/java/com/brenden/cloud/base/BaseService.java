package com.brenden.cloud.base;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/7
 */
public interface BaseService<T> extends IService<T> {

    int insertBatchSomeColumn(Collection<T> entityList);

    int insertBatchSomeColumn(Collection<T> entityList, int batchSize);

    T getByCode(Serializable code);

    boolean removeByCode(Serializable code);

    T getByFunction(SFunction<T, Object> column, Object val);

    List<T> listByFunction(SFunction<T, Object> column, Object... values);

    List<T> listByFunction(SFunction<T, Object> column, Collection<?> coll);

}
