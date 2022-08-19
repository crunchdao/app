package com.crunchdao.app.service.analytics.segment.consumer;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.crunchdao.app.service.analytics.segment.util.FluentHashMap;
import com.segment.analytics.Analytics;
import com.segment.analytics.messages.IdentifyMessage;
import com.segment.analytics.messages.TrackMessage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserEventConsumer {
	
	private final Analytics analytics;
	
	@RabbitListener(queues = "${app.messaging.queue.user.event.created}")
	public void onUserCreated(UserDto user) {
		log.info("UserEventConsumer.onUserCreated({})", user);
		
		if (user.getId() == null) {
			return;
		}
		
		analytics.enqueue(IdentifyMessage.builder()
			.userId(user.getId().toString())
			.traits(new FluentHashMap<String, Object>()
				.with(UserDto.Fields.username, user.getUsername())
				.with(UserDto.Fields.firstName, user.getFirstName())
				.with(UserDto.Fields.lastName, user.getLastName())
				.with(UserDto.Fields.email, user.getEmail())));
	}
	
	@RabbitListener(queues = "${app.messaging.queue.user.event.deleted}")
	public void onUserDeleted(UUID id) {
		log.info("UserEventConsumer.onUserDeleted({})", id);
		
		if (id == null) {
			return;
		}
		
		analytics.enqueue(TrackMessage.builder("User Deleted")
			.userId(id.toString()));
	}
	
	@Data
	@FieldNameConstants
	@Accessors(chain = true)
	public static class UserDto {
		
		private String id;
		private String username;
		private String firstName;
		private String lastName;
		private String email;
		private String createdAt;
		private String updatedAt;
		
	}
	
}