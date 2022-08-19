package com.crunchdao.app.service.analytics.segment.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.crunchdao.app.service.analytics.segment.util.FluentHashMap;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.segment.analytics.Analytics;
import com.segment.analytics.messages.TrackMessage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class ConnectionEventConsumer {
	
	private final Analytics analytics;
	
	@RabbitListener(queues = "${app.messaging.queue.connection.event.created}")
	public void onConnectionCreated(ConnectionDto connection) {
		log.info("ConnectionEventConsumer.onConnectionCreated({})", connection);
		
		if (connection.getUserId() == null) {
			return;
		}
		
		analytics.enqueue(TrackMessage.builder("Connection Created")
			.userId(connection.getUserId())
			.properties(new FluentHashMap<String, Object>()
				.with(ConnectionDto.Fields.type, connection.getType())
				.with(ConnectionDto.Fields.handle, connection.getHandle())
				.with(ConnectionDto.Fields.username, connection.getUsername())
				.with(ConnectionDto.Fields.profileUrl, connection.getProfileUrl())
				.with(ConnectionDto.Fields.isPublic, connection.getIsPublic())
				.with(ConnectionDto.Fields.createdAt, connection.getCreatedAt())
				.with(ConnectionDto.Fields.updatedAt, connection.getUpdatedAt())));
	}
	
	@RabbitListener(queues = "${app.messaging.queue.connection.event.deleted}")
	public void onConnectionDeleted(DeletedConnection connection) {
		log.info("ConnectionEventConsumer.onConnectionDeleted({})", connection);
		
		if (connection.getUserId() == null) {
			return;
		}
		
		analytics.enqueue(TrackMessage.builder("Connection Deleted")
			.userId(connection.getUserId())
			.properties(new FluentHashMap<String, Object>()
				.with(DeletedConnection.Fields.type, connection.getType())));
	}
	
	@Data
	@FieldNameConstants
	public static class ConnectionDto {
		
		private String userId;
		private String type;
		private String handle;
		private String username;
		private String profileUrl;
		@JsonProperty("public")
		private String isPublic;
		private String createdAt;
		private String updatedAt;
		
	}
	
	@Data
	@FieldNameConstants
	public static class DeletedConnection {
		
		private final String userId;
		private final String type;
		
	}
	
}