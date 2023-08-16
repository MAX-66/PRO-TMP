package com.brenden.cloud.knife4j.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2023/8/16
 */
@Configuration
public class Knife4jAutoConfig {


    @ConditionalOnMissingBean(Docket.class)
    @Bean
    Docket defaultApi2(CustomizedKnife4jProperties properties) {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //.title("swagger-bootstrap-ui-demo RESTful APIs")
                        .description(properties.getDescription())
                        .termsOfServiceUrl(properties.getUrl())
                        .contact(properties.getContact())
                        .version(properties.getVersion())
                        .license("Apache License 2.0")
                        .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                        .build())
                //分组名称
                .groupName("3.X版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage(properties.getApiPackage()))
//                .apis(properties.getApiPackage())
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

}
