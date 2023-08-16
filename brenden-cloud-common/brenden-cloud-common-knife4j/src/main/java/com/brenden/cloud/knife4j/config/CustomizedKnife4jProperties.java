package com.brenden.cloud.knife4j.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import springfox.documentation.service.Contact;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2023/8/16
 */
@Getter
@Setter
@Component
public class CustomizedKnife4jProperties {

    private Contact contact = new Contact("1", "2", "3");

    private String description = "买哦书";

    private String version = "买哦书";

    private String url = "买哦书";

    private String apiPackage = "com.brenden.cloud";

}
