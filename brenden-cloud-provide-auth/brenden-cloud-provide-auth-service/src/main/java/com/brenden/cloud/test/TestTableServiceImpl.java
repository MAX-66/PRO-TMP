package com.brenden.cloud.test;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.brenden.cloud.base.entity.PageEntity;
import com.brenden.cloud.core.utils.BeanCopyUtil;
import com.brenden.cloud.test.persistence.domain.TestTableDO;
import com.brenden.cloud.test.persistence.manager.TestTableManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import test.vo.EditTestTableReq;
import test.vo.SaveTestTableReq;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/6
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TestTableServiceImpl {


    private final TestTableManager testTableManager;


    public IPage<TestTableDO> page(PageEntity req) {
        IPage<TestTableDO> page = new Page<>(req.getPageNum(), req.getPageSize());
        return testTableManager.page(page);
    }


    public Boolean save(SaveTestTableReq req) {
        TestTableDO testTableDO = BeanCopyUtil.copyProperties(req, TestTableDO.class);
        return testTableManager.save(testTableDO);
    }

    public Boolean saveBatch(List<SaveTestTableReq> req) {
        Collection<TestTableDO> testTableDOS = BeanCopyUtil.copyProperties(req, TestTableDO.class);
        return testTableManager.saveBatch(testTableDOS);
    }

    public TestTableDO findById(Long id) {
        return testTableManager.getById(id);
    }

    public Boolean deleteById(Long id) {
        return testTableManager.removeById(id);
    }

    public Boolean updateById(EditTestTableReq req) {
        TestTableDO testTableDO = BeanCopyUtil.copyProperties(req, TestTableDO.class);
        return testTableManager.updateById(testTableDO);
    }
}
