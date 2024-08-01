package com.brenden.cloud.doc.config;

import com.brenden.cloud.doc.filter.JakartaBasicFilter;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2023/8/16
 */
@AutoConfiguration
@EnableConfigurationProperties({CustomizedSpringDocProperties.class, DocBasicProperties.class})
@ComponentScan("com.brenden.cloud.doc")
public class Knife4jAutoConfig {

    @ConditionalOnMissingBean(OpenAPI.class)
    @ConditionalOnProperty(prefix = "springdoc.info", value = "", matchIfMissing = true)
    @Bean
    public OpenAPI customOpenAPI(CustomizedSpringDocProperties properties) {
        return new OpenAPI()
                .info(new Info()
                        .title(properties.getTitle())
                        .version(properties.getVersion())
                        .contact(new Contact().name(properties.getContactName())
                                .url(properties.getContactUrl()).email(properties.getContactEmail()))
                        .description(properties.getDescription())
                        .license(new License().name("Apache License 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0")));
    }


    @Bean
    @ConditionalOnProperty(prefix = "springdoc.basic.enable", value = "true")
    public JakartaBasicFilter jakartaBasicFilter(DocBasicProperties properties) {
        JakartaBasicFilter jakartaBasicFilter = new JakartaBasicFilter();
        jakartaBasicFilter.setEnable(properties.isEnable());
        jakartaBasicFilter.setUsername(properties.getUsername());
        jakartaBasicFilter.setPassword(properties.getPassword());
        return jakartaBasicFilter;
    }

}
