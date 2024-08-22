package com.brenden.cloud.common.queue.constants;

/**
 * 消息常量类
 * @author lisuheng
 */
public interface QueueMessageConstants {


	/** 发送中 */
	String DICT_MQ_SEND_STATUS_SENDING = "1";
	/** 发送成功 */
	String DICT_MQ_SEND_STATUS_SUCCESS = "2";
	/** 发送失败 */
	String DICT_MQ_SEND_STATUS_FAIL = "3";

	/** 未到达 Broker */
	String DICT_MQ_STATUS_FAIL_NOT_TO_BROKER = "1";
	/** 失败后死信通知 */
	String DICT_MQ_STATUS_FAIL_DEAD_QUEUE = "2";


	/** 消费成功 */
	String DICT_MQ_CONSUME_STATUS_SUCCESS = "1";
	/** 消费失败 */
	String DICT_MQ_CONSUME_STATUS_FAIL = "2";

}
