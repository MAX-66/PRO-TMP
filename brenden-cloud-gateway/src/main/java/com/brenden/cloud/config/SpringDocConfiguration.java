package com.brenden.cloud.config;

import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.properties.AbstractSwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2024/1/3
 */
@Configuration
public class SpringDocConfiguration {

    private final static String API_URI = "/v3/api-docs";

    private final static String API_NAME_PREFIX = "api-";

    @Bean
    public List<GroupedOpenApi> apis(SwaggerUiConfigProperties swaggerUiConfigProperties, RouteDefinitionLocator routeDefinitionLocator) {
        List<GroupedOpenApi> groups = new ArrayList<>();
        Set<AbstractSwaggerUiConfigProperties.SwaggerUrl> urls = routeDefinitionLocator.getRouteDefinitions().map(routeDefinition -> {
            groups.add(GroupedOpenApi.builder().pathsToMatch("/" + routeDefinition.getId() + "/**").group(routeDefinition.getId()).build());
            return new AbstractSwaggerUiConfigProperties.SwaggerUrl(
                    routeDefinition.getId(),
                    routeDefinition.getId() + API_URI,
                    API_NAME_PREFIX.concat(routeDefinition.getId())
            );
        }).collect(Collectors.toSet()).block();
        swaggerUiConfigProperties.setUrls(urls);
        return groups;
    }

}
