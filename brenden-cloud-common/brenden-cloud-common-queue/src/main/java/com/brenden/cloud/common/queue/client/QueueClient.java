package com.brenden.cloud.common.queue.client;


import com.brenden.cloud.common.queue.entity.QueueMessage;

/**
 * 消息队列客户端
 *
 * @author lisuheng
 */
public interface QueueClient<T> {

	/**
	 * 发送消息
	 *
	 * @param queueMessage queueMessage
	 */
	void send(QueueMessage<?> queueMessage);

	/**
	 * 发送延迟消息
	 *
	 * @param queueMessage queueMessage
	 * @param delayTime 延迟时间，单位秒
	 */
	void sendDelay(QueueMessage<?> queueMessage, long delayTime);


	/**
	 * 发送事务消息
	 *
	 * @param queueMessage queueMessage
	 */
	void sendTransactional(QueueMessage<?> queueMessage);


	/**
	 * 出错消息备份，可以持久化，或发到其他备用队列
	 *
	 * @param queueMessage queueMessage
	 */
	void backup(QueueMessage<?> queueMessage);

}
