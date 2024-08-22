package com.brenden.cloud.common.queue.service.impl;


import com.brenden.cloud.common.queue.entity.QueueMessage;
import com.brenden.cloud.common.queue.service.QueueMessageService;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息持久化服务
 *
 * @author lisuheng
 */
@Slf4j
public class DefaultQueueMessageServiceImpl implements QueueMessageService {


	/**
	 * 持久化消息信息
	 *
	 * @param queueMessage queueMessage
	 */
	@Override
	public void save(QueueMessage<?> queueMessage) {
		log.info("持久化消息信息");
	}

	/**
	 * 更新消息消费状态
	 *
	 * @param code          code
	 * @param consumeStatus consumeStatus
	 */
	@Override
	public void updateConsumeStatusByCode(String code, String consumeStatus) {
		log.info("更新消息消费状态");
	}

	/**
	 * 更新消息发送状态
	 *
	 * @param code       code
	 * @param sendStatus sendStatus
	 */
	@Override
	public void updateSendStatusByCode(String code, String sendStatus) {
		log.info("更新消息发送状态");
	}

	/**
	 * 消息发送失败更新状态
	 *
	 * @param code       code
	 * @param failStatus failStatus
	 * @param failMsg    failMsg
	 */
	@Override
	public void updateSendFailByCode(String code, String failStatus, String failMsg) {
		log.info("消息发送失败更新状态");
	}

}
