package com.brenden.cloud.common.queue.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * QueueMessage
 *
 * @author lisuheng
 */
@Getter
@Setter
@ToString
public class QueueMessage<T extends Serializable> implements Serializable {

	@Serial
	private static final long serialVersionUID = 7631963781965555651L;

	private Boolean isPersistence = Boolean.TRUE;
	private String messageId = UUID.randomUUID().toString().replace("-", "");
	private String messageBizType;

	private String messageBizCode;
	private T messageData;
	private String target;

}
