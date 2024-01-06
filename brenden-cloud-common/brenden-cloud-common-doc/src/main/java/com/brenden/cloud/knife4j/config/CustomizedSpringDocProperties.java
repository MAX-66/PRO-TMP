package com.brenden.cloud.knife4j.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * api文档基本信息配置
 * </p>
 *
 * @author lxq
 * @since 2023/8/16
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "springdoc.info")
public class CustomizedSpringDocProperties {

    private String contactName;

    private String contactEmail;

    private String contactUrl;

    private String description;

    private String version;

    private String url;

    private String title;
}
