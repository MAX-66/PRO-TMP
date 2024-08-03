package com.brenden.cloud.base.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/6/20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContextPayload {

    private Long userId;

    private String username;

    private String key;

    private String token;

}
