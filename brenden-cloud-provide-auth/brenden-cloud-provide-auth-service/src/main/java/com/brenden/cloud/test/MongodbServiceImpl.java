package com.brenden.cloud.test;

import com.brenden.cloud.base.entity.PageEntity;
import com.brenden.cloud.mongodb.entity.MongoPageEntity;
import com.brenden.cloud.test.persistence.model.User;
import com.brenden.cloud.test.persistence.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/22
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class MongodbServiceImpl {

    private final TestRepository testRepository;

    public void save() {
        User user = new User();
        user.setId("1");
        user.setName("2323");
        testRepository.save(user);
    }

    public MongoPageEntity<User> query() {
        PageEntity pageEntity = new PageEntity();
        pageEntity.setPageSize(10L);
        pageEntity.setPageNum(1L);
        return testRepository.queryPage(pageEntity, new ArrayList<>());
    }

}
