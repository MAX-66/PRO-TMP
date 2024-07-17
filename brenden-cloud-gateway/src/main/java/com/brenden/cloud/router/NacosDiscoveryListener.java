package com.brenden.cloud.router;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import io.micrometer.common.lang.NonNullApi;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.brenden.cloud.constant.GatewayConstant.DATA_ID;
import static com.brenden.cloud.constant.GatewayConstant.ROUTER_KEY;

@Component
@Slf4j
@NonNullApi
public class NacosDiscoveryListener implements ApplicationEventPublisherAware {

    private final NacosConfigManager nacosConfigManager;

    private final NacosConfigProperties nacosConfigProperties;

    private final ReactiveHashOperations<String, String, RouteDefinition> reactiveHashOperations;

    private ApplicationEventPublisher applicationEventPublisher;


    public NacosDiscoveryListener(ReactiveRedisTemplate<String, Object> reactiveRedisTemplate, NacosConfigManager nacosConfigManager) {
        this.reactiveHashOperations = reactiveRedisTemplate.opsForHash();
        this.nacosConfigManager = nacosConfigManager;
        this.nacosConfigProperties = nacosConfigManager.getNacosConfigProperties();
    }

    @PostConstruct
    public void init() throws NacosException {
        log.info("router init");
        ConfigService configService = nacosConfigManager.getConfigService();
        String group = nacosConfigProperties.getGroup();
        configService.addListener(DATA_ID, group, new Listener() {
            @Override
            public Executor getExecutor() {
                return Executors.newSingleThreadExecutor();
            }
            @Override
            public void receiveConfigInfo(String configInfo) {
                log.info("[dataId]:[{}],[group:]:[{}]Configuration changed to:{}", DATA_ID, group, configInfo);
                // 删除后会自动重新读取
                reactiveHashOperations.delete(ROUTER_KEY)
                        .subscribe(success -> log.info("save router success"), error -> {
                            log.error("save router fail: cause:");
                            log.error("", error);
                        });
                applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            }
        });
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }



}
