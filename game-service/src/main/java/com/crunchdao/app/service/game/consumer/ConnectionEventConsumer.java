package com.crunchdao.app.service.game.consumer;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.crunchdao.app.service.game.configuration.AchievementIdsConfigurationProperties;
import com.crunchdao.app.service.game.service.RabbitMQSender;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class ConnectionEventConsumer {
	
	private final RabbitMQSender rabbitMQSender;
	private final AchievementIdsConfigurationProperties achievementIds;
	
	@RabbitListener(queues = "${app.messaging.queue.connection.event.created}")
	public void onConnectionCreated(ConnectionDto connection) {
		log.info("ConnectionEventConsumer.onConnectionCreated({})", connection);
		
		if (connection.getUserId() == null) {
			return;
		}
		
		rabbitMQSender.sendAchievementIncrement(RabbitMQSender.IncrementCommand.builder()
			.achievementId(getAchievementId(connection.getType()))
			.userId(connection.getUserId())
			.at(connection.getCreatedAt())
			.build());
	}
	
	public UUID getAchievementId(String type) {
		switch (type) {
			case "DISCORD": {
				return achievementIds.getConnectDiscord();
			}
			
			case "TWITTER": {
				return achievementIds.getConnectTwitter();
			}
			
			case "GITHUB": {
				return achievementIds.getConnectGithub();
			}
			
			default: {
				throw new IllegalArgumentException("unknown type: " + type);
			}
		}
	}
	
	@Data
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ConnectionDto {
		
		private UUID userId;
		private String type;
		private LocalDateTime createdAt;
		
	}
	
}