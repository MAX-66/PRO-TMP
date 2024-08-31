package com.brenden.cloud.base;

import java.io.Serializable;
import java.util.Collection;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import org.apache.ibatis.annotations.Param;

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

    T findByCode(@Param("code") Serializable code);

	int deleteByCode(@Param("code") Serializable code);

	Integer isExists(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

	T selectOne(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper);

	@Override
	default boolean exists(Wrapper<T> queryWrapper) {
		return SqlHelper.retBool(this.isExists(queryWrapper));
	}

}
