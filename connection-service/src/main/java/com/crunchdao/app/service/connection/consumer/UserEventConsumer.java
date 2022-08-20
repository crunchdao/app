package com.crunchdao.app.service.connection.consumer;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.crunchdao.app.service.connection.service.ConnectionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserEventConsumer {
	
	private final ConnectionService connectionService;
	
	@RabbitListener(queues = "${app.messaging.queue.user.event.deleted}")
	public void onUserDeleted(UUID id) {
		log.info("UserEventConsumer.onUserDeleted({})", id);
		
		connectionService.disconnectAll(id);
	}
	
}