package com.brenden.cloud;

import com.brenden.cloud.test.TestTableServiceImpl;
import com.brenden.cloud.test.persistence.manager.TestTableManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import test.vo.EditTestTableReq;
import test.vo.SaveTestTableReq;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TestTableServiceImplTest {

    @Mock
    private TestTableManager testTableManager;

    @InjectMocks
    @Spy
    private TestTableServiceImpl testTableServiceImpl;

    private SaveTestTableReq saveTestTableReq;
    private EditTestTableReq editTestTableReq;
    private List<SaveTestTableReq> saveTestTableReqList;

    @BeforeEach
    void setUp() {
        saveTestTableReq = new SaveTestTableReq();
        saveTestTableReq.setFieldA("a");
        saveTestTableReq.setFieldB("b");
        saveTestTableReq.setFieldC("c");
        saveTestTableReq.setFieldD("d");
        saveTestTableReqList = new ArrayList<>();
        saveTestTableReqList.add(saveTestTableReq);
    }

    @Test
    void testSave() {
        Mockito.doReturn(true).when(testTableServiceImpl).save(Mockito.any(SaveTestTableReq.class));
        Boolean save = testTableServiceImpl.save(saveTestTableReq);
        Assertions.assertTrue(save);
    }

    @Test
    void testSaveBatch() {
        Mockito.doReturn(true).when(testTableServiceImpl).saveBatch(saveTestTableReqList);
        Boolean b = testTableServiceImpl.saveBatch(saveTestTableReqList);
        Assertions.assertTrue(b);
    }

    @Test
    void testFindById() {

    }

    @Test
    void testDeleteById() {

    }

    @Test
    void testUpdateById() {

    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme