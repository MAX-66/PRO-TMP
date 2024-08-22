package com.brenden.cloud.test.persistence.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/22
 */
@Data
public class User {
    @Id
    private String id;

    private String name;
}
