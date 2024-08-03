package com.brenden.cloud.base.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/7/29
 */
@Data
@Schema(description = "统一参数父类")
public class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 2851506670307099952L;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.NOT_REQUIRED, hidden = true)
    private Long userId;

    @Schema(description = "用户名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED, hidden = true)
    private String username;

    @Schema(description = "token", requiredMode = Schema.RequiredMode.NOT_REQUIRED, hidden = true)
    private String token;

    @Schema(description = "秒级时间戳", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long timestamp;

    @Schema(description = "签名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String signature;

    public BaseEntity() {

    }


    public BaseEntity(Long userId, String username, String token) {
        this.userId = userId;
        this.username = username;
        this.token = token;
    }

    public BaseEntity(Long userId, String username, String token, Long timestamp) {
        this.userId = userId;
        this.username = username;
        this.token = token;
        this.timestamp = timestamp;
    }
}
