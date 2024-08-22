package com.brenden.cloud.mongodb.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/22
 */
@Data
public class QueryEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -3895208548931892262L;

    private String field;

    private String value;
}
