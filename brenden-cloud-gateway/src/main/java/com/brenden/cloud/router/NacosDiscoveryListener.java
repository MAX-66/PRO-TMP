package com.brenden.cloud.router;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import io.micrometer.common.lang.NonNullApi;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static com.alibaba.nacos.client.config.common.ConfigConstants.DATA_ID;

@Component
@Slf4j
@NonNullApi
@RequiredArgsConstructor
public class NacosDiscoveryListener implements ApplicationEventPublisherAware {

    private final NacosConfigManager nacosConfigManager;

    private final NacosConfigProperties nacosConfigProperties;

    private ApplicationEventPublisher applicationEventPublisher;

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
                applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this));
            }
        });
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }



}
