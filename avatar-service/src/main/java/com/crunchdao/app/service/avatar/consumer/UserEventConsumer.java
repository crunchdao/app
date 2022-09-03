package com.crunchdao.app.service.avatar.consumer;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.crunchdao.app.service.avatar.service.AvatarService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserEventConsumer {
	
	private final AvatarService service;
	
	@RabbitListener(queues = "${app.messaging.queue.user.event.created}")
	public void onUserCreated(UserDto user) {
		log.info("UserEventConsumer.onUserCreated({})", user);
		
		service.uploadFromTemplate(user.getId());
	}
	
	@Data
	@Accessors(chain = true)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class UserDto {
		
		private UUID id;
		
	}
	
}