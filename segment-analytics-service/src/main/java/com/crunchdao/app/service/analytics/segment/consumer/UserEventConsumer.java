package com.crunchdao.app.service.analytics.segment.consumer;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

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
		
		analytics.enqueue(IdentifyMessage.builder()
			.userId(user.getId().toString())
			.traits(Map.ofEntries(
				Map.entry(UserDto.Fields.username, user.getUsername()),
				Map.entry(UserDto.Fields.firstName, user.getFirstName()),
				Map.entry(UserDto.Fields.lastName, user.getLastName()),
				Map.entry(UserDto.Fields.email, user.getEmail()))));
	}
	
	@RabbitListener(queues = "${app.messaging.queue.user.event.deleted}")
	public void onUserDeleted(UUID id) {
		log.info("UserEventConsumer.onUserDeleted({})", id);
		
		analytics.enqueue(TrackMessage.builder("User Deleted")
			.userId(id.toString()));
	}
	
	@Data
	@FieldNameConstants
	@Accessors(chain = true)
	public static class UserDto {
		
		private UUID id;
		private String username;
		private String firstName;
		private String lastName;
		private String email;
		private LocalDateTime createdAt;
		private LocalDateTime updatedAt;
		
	}
	
}