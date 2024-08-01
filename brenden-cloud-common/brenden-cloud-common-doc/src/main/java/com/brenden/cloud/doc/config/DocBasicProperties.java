package com.brenden.cloud.doc.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/8/1
 */

@Getter
@Setter
@ConfigurationProperties(prefix = "springdoc.basic")
public class DocBasicProperties {

    private boolean enable = false;

    private String username;

    private String password;

}
