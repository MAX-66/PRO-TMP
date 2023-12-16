package com.brenden.cloud.router;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.brenden.cloud.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Collections;
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
@Component
public class CustomizeRouteDefinitionRepository implements RouteDefinitionRepository {

    private final ReactiveHashOperations<String, String, RouteDefinition> reactiveHashOperations;

    private final NacosConfigManager nacosConfigManager;

    private final NacosConfigProperties nacosConfigProperties;

    public CustomizeRouteDefinitionRepository(ReactiveRedisTemplate<String, Object> reactiveRedisTemplate, NacosConfigManager nacosConfigManager) {
        this.reactiveHashOperations = reactiveRedisTemplate.opsForHash();
        this.nacosConfigManager = nacosConfigManager;
        this.nacosConfigProperties = nacosConfigManager.getNacosConfigProperties();
    }


    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        return reactiveHashOperations.entries(ROUTER_KEY).map(Map.Entry::getValue).switchIfEmpty(routeDefinitionFlux());
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return route.flatMap(routeDefinition ->
                reactiveHashOperations.put(ROUTER_KEY, routeDefinition.getId(), routeDefinition)
                        .doOnSuccess(success -> log.info("save router success"))
                        .doOnError(error -> {
                            log.error("save router fail: cause:");
                            log.error("", error);
                        })
                        .then(Mono.empty()));
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return routeId.flatMap(id ->
                reactiveHashOperations.remove(ROUTER_KEY, id)
                        .doOnSuccess(success -> log.info("save router success"))
                        .doOnError(error -> {
                            log.error("save router fail: cause:");
                            log.error("", error);
                        })
                        .then(Mono.empty()));

    }


    private Flux<RouteDefinition> routeDefinitionFlux() {
        return Flux.fromIterable(getDefaultRouter()).publishOn(Schedulers.boundedElastic())
                .doOnNext(routeDefinition ->
                        reactiveHashOperations.putIfAbsent(ROUTER_KEY, routeDefinition.getId(), routeDefinition)
                                .subscribe(success -> log.info("push router success"), error -> {
                                    log.error("push router fail: cause:");
                                    log.error("", error);
                                }));
    }

    private List<RouteDefinition> getDefaultRouter() {
        try {
            String group = nacosConfigProperties.getGroup();
            ConfigService configService = nacosConfigManager.getConfigService();
            String config = configService.getConfig(ROUTER_KEY, group, 10000);
            if (ObjectUtils.isEmpty(config)) {
                return Collections.emptyList();
            }
            return JacksonUtil.toList(config, RouteDefinition.class);
        } catch (Exception e) {
            log.error("Failed to retrieve default route data -> {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
