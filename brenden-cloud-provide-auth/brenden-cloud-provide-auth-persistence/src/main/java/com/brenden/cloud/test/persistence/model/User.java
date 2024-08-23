package com.brenden.cloud.test.persistence.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

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
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = -1780850990143301180L;
    @Id
    private String id;

    private String name;
}
