package com.brenden.cloud.base.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/9
 */

@Data
public class PageEntity extends BaseEntity{
    @Serial
    private static final long serialVersionUID = 828380207833179503L;

    @Schema(description = "页大小", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long pageSize;

    @Schema(description = "页码", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long pageNum;

    @Schema(description = "排序字段", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orderBy;

    @Schema(description = "排序方式：正序true；倒序false", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean sort;
}
