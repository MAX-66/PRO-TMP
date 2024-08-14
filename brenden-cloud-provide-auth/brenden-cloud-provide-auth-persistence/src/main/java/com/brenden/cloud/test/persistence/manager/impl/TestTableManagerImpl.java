package com.brenden.cloud.test.persistence.manager.impl;

import com.brenden.cloud.base.BaseServiceImpl;
import com.brenden.cloud.test.persistence.domain.TestTableDO;
import com.brenden.cloud.test.persistence.mapper.TestTableMapper;
import com.brenden.cloud.test.persistence.manager.TestTableManager;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author brenden
 * @since 2024-08-06
 */
@Service
public class TestTableManagerImpl extends BaseServiceImpl<TestTableMapper, TestTableDO> implements TestTableManager {

}
