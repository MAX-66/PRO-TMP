package com.brenden.cloud.common.queue.service;


import com.brenden.cloud.common.queue.entity.QueueMessage;

/**
 * 消息持久化服务
 *
 * @author lisuheng
 */
public interface QueueMessageService {

	/**
	 * 持久化消息信息
	 *
	 * @param queueMessage queueMessage
	 */
	void save(QueueMessage<?> queueMessage);

	/**
	 * 更新消息消费状态
	 *
	 * @param code code
	 * @param consumeStatus consumeStatus
	 */
	void updateConsumeStatusByCode(String code, String consumeStatus);

	/**
	 * 更新消息发送状态
	 *
	 * @param code code
	 * @param sendStatus sendStatus
	 */
	void updateSendStatusByCode(String code, String sendStatus);

	/**
	 * 消息发送失败更新状态
	 *
	 * @param code code
	 * @param failStatus failStatus
	 * @param failMsg failMsg
	 */
	void updateSendFailByCode(String code, String failStatus, String failMsg);

}
