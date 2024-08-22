package com.brenden.cloud.common.queue.config;

import com.brenden.cloud.common.queue.service.QueueMessageService;
import com.brenden.cloud.common.queue.service.impl.DefaultQueueMessageServiceImpl;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Queue 配置类
 *
 * @author lisuheng
 */
@Configuration
public class QueueConfig {

	@Bean
	@ConditionalOnMissingBean(QueueMessageService.class)
	public QueueMessageService queueMessageService() {
		return new DefaultQueueMessageServiceImpl();
	}

}
