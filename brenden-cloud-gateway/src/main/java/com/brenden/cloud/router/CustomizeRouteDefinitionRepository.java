package com.brenden.cloud.router;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.brenden.cloud.utils.JacksonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;

import static com.brenden.cloud.constant.RedisKeyConstant.ROUTER_KEY;

/**
 * <p>
 *
 * </p>
 *
 * @author lxq
 * @since 2023/12/7
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class CustomizeRouteDefinitionRepository implements RouteDefinitionRepository {

    private final ReactiveHashOperations<String, String, RouteDefinition> reactiveHashOperations;

    private final NacosConfigManager nacosConfigManager;

    private final NacosConfigProperties nacosConfigProperties;




    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return reactiveHashOperations.entries(ROUTER_KEY).map(Map.Entry::getValue).switchIfEmpty(routeDefinitionFlux());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }


    private Flux<RouteDefinition> routeDefinitionFlux() {
        return Flux.fromIterable(getDefaultRouter()).publishOn(Schedulers.boundedElastic()).doOnNext(routeDefinition -> {
            reactiveHashOperations.putIfAbsent(ROUTER_KEY, routeDefinition.getId(), routeDefinition)
                    .subscribe(success -> {
                        log.info("push router success");
                    }, error -> {
                        log.error("push router fail: cause:");
                        log.error("", error);
                    });
        });
    }

    private List<RouteDefinition> getDefaultRouter() {
        try {
            String group = nacosConfigProperties.getGroup();
            ConfigService configService = nacosConfigManager.getConfigService();
            String config = configService.getConfig("", group, 10000);
            return JacksonUtil.toList(config, RouteDefinition.class);
        } catch (Exception e) {
            log.error("Failed to retrieve default route data -> {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
