package com.brenden.cloud.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.brenden.cloud.base.entity.PageEntity;
import com.brenden.cloud.base.entity.ResultEntity;
import com.brenden.cloud.core.annotation.ApiProcess;
import com.brenden.cloud.test.TestTableServiceImpl;
import com.brenden.cloud.test.persistence.domain.TestTableDO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import test.vo.EditTestTableReq;
import test.vo.SaveTestTableReq;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/6
 */
@RestController
@RequestMapping("/table")
@RequiredArgsConstructor
@Tag(name = "table操作")
public class TestTableController {

    private final TestTableServiceImpl  testTableService;
    @PostMapping("/save")
    @Operation(description = "保存数据")
    public ResultEntity<Boolean> save(@RequestBody SaveTestTableReq req) {
        return ResultEntity.success(testTableService.save(req));
    }

    @GetMapping("/page")
    @Operation(description = "分页")
    public ResultEntity<IPage<TestTableDO>> save( PageEntity req) {
        return ResultEntity.success(testTableService.page(req));
    }


    @PostMapping("/save-batch")
    @Operation(description = "保存数据")
    public ResultEntity<Boolean> saveBatch(@RequestBody List<SaveTestTableReq> req) {
        return ResultEntity.success(testTableService.saveBatch(req));
    }

    @GetMapping("/find")
    @Operation(description = "查找数据")
    public ResultEntity<TestTableDO> find(Long id) {
        return ResultEntity.success(testTableService.findById(id));
    }


    @DeleteMapping("/delete")
    @Operation(description = "删除数据")
    public ResultEntity<Boolean> delete(Long id) {
        return ResultEntity.success(testTableService.deleteById(id));
    }

    @PutMapping("/edit")
    @Operation(description = "删除数据")
    public ResultEntity<Boolean> edit(@RequestBody EditTestTableReq req) {
        return ResultEntity.success(testTableService.updateById(req));
    }

}
