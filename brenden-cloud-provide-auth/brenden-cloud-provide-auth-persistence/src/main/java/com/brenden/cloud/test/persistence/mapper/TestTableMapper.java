package com.brenden.cloud.test.persistence.mapper;

import com.brenden.cloud.base.BaseDao;
import com.brenden.cloud.test.persistence.domain.TestTableDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author brenden
 * @since 2024-08-06
 */
@Mapper
public interface TestTableMapper extends BaseDao<TestTableDO> {

}
