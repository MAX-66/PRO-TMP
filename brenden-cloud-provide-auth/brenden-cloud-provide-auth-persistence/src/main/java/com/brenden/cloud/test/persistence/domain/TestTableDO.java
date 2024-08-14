package com.brenden.cloud.test.persistence.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.brenden.cloud.entity.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

/**
 * <p>
 * 
 * </p>
 *
 * @author brenden
 * @since 2024-08-06
 */
@Getter
@Setter
@TableName("t_test_table")
public class TestTableDO extends BaseDO {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 字段a
     */
    @TableField("field_a")
    private String fieldA;

    /**
     * 字段b
     */
    @TableField("field_b")
    private String fieldB;

    /**
     * 字段c
     */
    @TableField("field_c")
    private String fieldC;

    /**
     * 字段d
     */
    @TableField("field_d")
    private String fieldD;
}
