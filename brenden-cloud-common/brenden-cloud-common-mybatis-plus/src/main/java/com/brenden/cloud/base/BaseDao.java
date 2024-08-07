package com.brenden.cloud.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/7
 */
public interface BaseDao<T> extends BaseMapper<T> {

    Integer insertBatchSomeColumn(@Param("list") Collection<T> entityList);

}
